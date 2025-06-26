package io.lolyay.lyrics.live;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SyncedLyricsPlayer {

    private final List<LyricData.LyricSection> sections;
    private final List<String> allLinesText = new ArrayList<>();
    private final List<TimedLyric> timedLines = new ArrayList<>();

    // We'll extract these to build the embed
    private final String title;
    private final String author;
    private final String source;

    // A scheduler to run tasks in the future without blocking
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private SyncedLyricsPlayer(List<LyricData.LyricSection> sections, String title, String author, String source) {
        this.sections = sections;
        this.title = title;
        this.author = author;
        this.source = source;
        flattenLyrics();
    }

    /**
     * Parses the full JSON string and creates a SyncedLyricsPlayer instance.
     *
     * @param fullJsonString The complete JSON from your source.
     * @return A new instance of SyncedLyricsPlayer.
     */
    public static SyncedLyricsPlayer fromJson(String fullJsonString) {
        // Use org.json to navigate the complex path
        JSONObject root = new JSONObject(fullJsonString);
        JSONObject pageProps = root.getJSONObject("props").getJSONObject("pageProps");
        JSONObject trackData = pageProps.getJSONObject("data").getJSONObject("trackInfo").getJSONObject("data");
        JSONArray trackStructureList = trackData.getJSONArray("trackStructureList");

        // Extract metadata for the embed
        String songTitle = pageProps.getJSONObject("data").getJSONObject("trackInfo").getJSONObject("data").getJSONObject("track").getString("name");
        String songAuthor = pageProps.getJSONObject("data").getJSONObject("trackInfo").getJSONObject("data").getJSONObject("track").getString("artistName");
        String sourceUrl = pageProps.getJSONObject("data").getJSONObject("trackInfo").getJSONObject("data").getJSONObject("track").getString("vanityId");
        String source = "musixmatch.com/" + sourceUrl;

        // Use Gson to map the final array to our records
        Gson gson = new Gson();
        List<LyricData.LyricSection> parsedSections = gson.fromJson(
                trackStructureList.toString(),
                new TypeToken<List<LyricData.LyricSection>>() {
                }.getType()
        );

        return new SyncedLyricsPlayer(parsedSections, songTitle, songAuthor, source);
    }

    /**
     * Flattens the nested sections/lines into two simple lists:
     * 1. allLinesText: A list of all lyric strings.
     * 2. timedLines: A list of objects containing the timestamp and index for scheduling.
     */
    private void flattenLyrics() {
        AtomicInteger globalIndex = new AtomicInteger(0);
        for (LyricData.LyricSection section : sections) {
            allLinesText.add("[" + section.title() + "]"); // Add section title like [verse]
            globalIndex.getAndIncrement(); // This title counts as a "line" in the final display

            for (LyricData.LyricLine line : section.lines()) {
                // If a line is instrumental, it might not have text but will have time.
                String text = (line.text() != null) ? line.text() : "♪ ♪ ♪";
                allLinesText.add(text);

                // Only schedule updates for lines that have a timestamp
                if (line.time() != null) {
                    timedLines.add(new TimedLyric(line.time().total(), globalIndex.get()));
                }
                globalIndex.getAndIncrement();
            }
            allLinesText.add(""); // Add a blank line between sections
            globalIndex.getAndIncrement();
        }
    }

    /**
     * Starts the synchronized lyric playback on a given Discord message.
     * It first sends an initial message with all lyrics, then schedules updates to highlight each line.
     *
     * @param message The JDA Message object to edit.
     */
    public void startPlayback(Message message, long songStartTimeMillis) {
        // 1. Calculate how long the song has already been playing.
        long currentTimeMillis = System.currentTimeMillis();
        double elapsedTimeSeconds = (currentTimeMillis - songStartTimeMillis) / 1000.0;

        // 2. Find which line should be highlighted *right now*.
        // We find the last lyric whose timestamp is before the current elapsed time.
        int initialHighlightIndex = -1;
        for (TimedLyric timedLine : timedLines) {
            if (timedLine.timestamp() <= elapsedTimeSeconds) {
                initialHighlightIndex = timedLine.globalIndex();
            } else {
                // Since the list is sorted by time, we can stop once we pass the current time.
                break;
            }
        }

        // 3. Send the initial embed with the correct line already highlighted.
        MessageEmbed initialEmbed = generateEmbedForLine(initialHighlightIndex);
        message.editMessage("# LIVE:").queue();
        message.editMessageEmbeds(initialEmbed).queue();

        // 4. Schedule all *future* updates.
        for (TimedLyric timedLine : timedLines) {
            // The lyric's absolute target time in milliseconds from the song's start.
            long lyricTargetMillis = (long) (timedLine.timestamp() * 1000);

            // The delay from *now* until the lyric should be highlighted.
            long delayMillis = (songStartTimeMillis + lyricTargetMillis) - currentTimeMillis;

            if (delayMillis > 0) { // Only schedule future events
                Runnable updateTask = () -> {
                    MessageEmbed updatedEmbed = generateEmbedForLine(timedLine.globalIndex());
                    message.editMessageEmbeds(updatedEmbed).queue(null, (error) -> {
                        System.err.println("Failed to edit message (it may have been deleted). Stopping playback.");
                        stop();
                    });
                };
                scheduler.schedule(updateTask, delayMillis, TimeUnit.MILLISECONDS);
            }
        }

        // 5. Schedule a final task to un-highlight everything and shut down.
        if (!timedLines.isEmpty()) {
            double lastTimestamp = timedLines.get(timedLines.size() - 1).timestamp();
            long finalTargetMillis = (long) ((lastTimestamp + 5) * 1000); // 5 seconds after last line
            long finalDelay = (songStartTimeMillis + finalTargetMillis) - currentTimeMillis;

            if (finalDelay > 0) {
                scheduler.schedule(() -> {
                    message.editMessageEmbeds(generateEmbedForLine(-1)).queue(null, e -> {
                    });
                    stop();
                }, finalDelay, TimeUnit.MILLISECONDS);
            } else {
                // If the song is already over, just stop immediately.
                stop();
            }
        } else {
            stop();
        }
    }

    /**
     * Stops all scheduled playback tasks and shuts down the scheduler.
     */
    public void stop() {
        if (!scheduler.isShutdown()) {
            scheduler.shutdownNow();
            System.out.println("Lyrics playback stopped and scheduler shut down.");
        }
    }

    /**
     * Generates the entire MessageEmbed for a specific state of the playback.
     * It re-uses the logic from your provided LyricsEmbedGenerator.
     *
     * @param highlightedIndex The index of the line to highlight with bold markdown. -1 for no highlight.
     * @return The generated MessageEmbed.
     */
    private MessageEmbed generateEmbedForLine(int highlightedIndex) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(this.title + " by " + this.author);
        builder.setFooter(this.source);

        // Build the full lyrics string with the current line highlighted
        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < allLinesText.size(); i++) {
            if (i == highlightedIndex) {
                contentBuilder.append("**").append(allLinesText.get(i)).append("**").append("\n");
            } else {
                contentBuilder.append(allLinesText.get(i)).append("\n");
            }
        }

        // --- Logic adapted from your LyricsEmbedGenerator ---
        final int MAX_FIELD_LENGTH = 1023;
        final String INVISIBLE_SEPARATOR = "\u200B";

        String fullContent = contentBuilder.toString();
        if (fullContent.length() <= MAX_FIELD_LENGTH) {
            builder.setDescription(fullContent);
        } else {
            // If content is too long for the description, split it into fields
            String[] lines = fullContent.split("\n");
            StringBuilder currentField = new StringBuilder();
            boolean isFirstField = true;

            for (String line : lines) {
                if (currentField.length() + line.length() + 1 > MAX_FIELD_LENGTH) {
                    builder.addField(isFirstField ? "" : INVISIBLE_SEPARATOR, currentField.toString().trim(), false);
                    isFirstField = false;
                    currentField.setLength(0);
                }
                currentField.append(line).append("\n");
            }
            if (!currentField.isEmpty()) {
                builder.addField(isFirstField ? "" : INVISIBLE_SEPARATOR, currentField.toString().trim(), false);
            }
        }

        return builder.build();
    }

    private record TimedLyric(double timestamp, int globalIndex) {
    }
}