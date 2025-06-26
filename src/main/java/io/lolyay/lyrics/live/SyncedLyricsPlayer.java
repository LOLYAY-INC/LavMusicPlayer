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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A self-managing class for synchronized lyrics playback.
 * It handles multiple guilds simultaneously using a static manager pattern.
 * You only need to interact with the public static methods.
 */
public class SyncedLyricsPlayer {


    private static final Map<Long, SyncedLyricsPlayer> activePlayers = new ConcurrentHashMap<>();
    private Message message;
    private ScheduledExecutorService scheduler;
    private String title;
    private String author;
    private String source;
    private boolean isPaused = false;
    private long songStartTimeMillis;
    private long pauseStartTimeMillis;
    /**
     * Private constructor to prevent manual instantiation.
     */
    private SyncedLyricsPlayer() {
    }
    private final List<String> allLinesText = new ArrayList<>();
    private final List<TimedLyric> timedLines = new ArrayList<>();

    /**
     * Starts or updates a lyrics playback session for a guild.
     * If a session is already active for the guild, it transitions to the new track.
     * Otherwise, it starts a new session.
     *
     * @param message             The message to display and edit lyrics on.
     * @param jsonString          The JSON data for the track.
     * @param songStartTimeMillis The System.currentTimeMillis() when the track started.
     */
    public static void start(Message message, String jsonString, long songStartTimeMillis) {
        long guildId = message.getGuild().getIdLong();

        SyncedLyricsPlayer player = activePlayers.computeIfAbsent(guildId, id -> new SyncedLyricsPlayer());
        player.loadAndPlay(message, jsonString, songStartTimeMillis);
    }

    public static void nextSong(long guildId, String jsonString, long songStartTimeMillis) {
        SyncedLyricsPlayer player = activePlayers.get(guildId);
        if (player == null) {
            System.err.println("nextSong called for guild " + guildId + ", but no active player was found. Use start() first.");
            return;
        }

        player.loadAndPlay(player.message, jsonString, songStartTimeMillis);
    }

    /**
     * Stops the playback session for a given guild, deleting the message.
     *
     * @param guildId The ID of the guild where playback should be stopped.
     */
    public static void stop(long guildId) {
        SyncedLyricsPlayer player = activePlayers.get(guildId);
        if (player != null) {
            player.performStop();
        }
    }

    public static void pause(long guildId) {
        SyncedLyricsPlayer player = activePlayers.get(guildId);
        if (player != null && !player.isPaused) {
            player.performPause();
        }
    }

    public static void resume(long guildId) {
        SyncedLyricsPlayer player = activePlayers.get(guildId);
        if (player != null && player.isPaused) {
            player.performResume();
        }
    }

    public static boolean isPaused(long guildId) {
        SyncedLyricsPlayer player = activePlayers.get(guildId);
        return player != null && player.isPaused;
    }

    /**
     * Checks if a lyrics session is currently active in a given guild.
     *
     * @param guildId The ID of the guild to check.
     * @return true if a session is live, false otherwise.
     */
    public static boolean isLive(long guildId) {
        return activePlayers.containsKey(guildId);
    }

    /**
     * The main instance logic for loading and playing a track.
     * This is called by the static start() method.
     */
    private void loadAndPlay(Message message, String jsonString, long songStartTimeMillis) {
        if (this.scheduler != null && !this.scheduler.isShutdown()) {
            this.scheduler.shutdownNow();
        }

        this.message = message;
        this.songStartTimeMillis = songStartTimeMillis;
        this.isPaused = false;

        parseAndLoadTrack(jsonString);
        beginScheduling();
    }

    /**
     * The internal stop logic that cleans up resources and removes from the static map.
     */
    private void performStop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        if (message != null) {
            long guildId = message.getGuild().getIdLong();
            message.delete().queue();
            activePlayers.remove(guildId);
            System.out.println("Lyrics playback stopped for guild " + guildId);
        }
    }

    private void performPause() {
        this.isPaused = true;
        this.pauseStartTimeMillis = System.currentTimeMillis();

        if (this.scheduler != null && !this.scheduler.isShutdown()) {
            this.scheduler.shutdownNow();
        }


        double elapsedTimeSeconds = (this.pauseStartTimeMillis - this.songStartTimeMillis) / 1000.0;
        int lastHighlightIndex = -1;
        for (TimedLyric timedLine : timedLines) {
            if (timedLine.timestamp() <= elapsedTimeSeconds) {
                lastHighlightIndex = timedLine.globalIndex();
            } else {
                break;
            }
        }
        message.editMessageEmbeds(generateEmbedForLine(lastHighlightIndex)).queue();
        System.out.println("Lyrics paused for guild " + message.getGuild().getIdLong());
    }

    /**
     * Resumes playback by shifting the start time and re-scheduling future events.
     */
    private void performResume() {
        long pauseDurationMillis = System.currentTimeMillis() - this.pauseStartTimeMillis;
        this.songStartTimeMillis += pauseDurationMillis;
        this.isPaused = false;


        beginScheduling();
        System.out.println("Lyrics resumed for guild " + message.getGuild().getIdLong());
    }

    private void parseAndLoadTrack(String fullJsonString) {
        allLinesText.clear();
        timedLines.clear();

        JSONObject root = new JSONObject(fullJsonString);
        JSONObject pageProps = root.getJSONObject("props").getJSONObject("pageProps");
        JSONObject trackData = pageProps.getJSONObject("data").getJSONObject("trackInfo").getJSONObject("data");
        JSONArray trackStructureList = trackData.getJSONArray("trackStructureList");
        this.title = trackData.getJSONObject("track").getString("name");
        this.author = trackData.getJSONObject("track").getString("artistName");
        String sourceUrl = trackData.getJSONObject("track").getString("vanityId");
        this.source = "musixmatch.com/" + sourceUrl;
        Gson gson = new Gson();
        List<LyricData.LyricSection> sections = gson.fromJson(trackStructureList.toString(), new TypeToken<List<LyricData.LyricSection>>() {
        }.getType());
        flattenLyrics(sections);
    }

    private void flattenLyrics(List<LyricData.LyricSection> sections) {
        AtomicInteger globalIndex = new AtomicInteger(0);
        for (LyricData.LyricSection section : sections) {
            allLinesText.add("[" + section.title() + "]");
            globalIndex.getAndIncrement();
            for (LyricData.LyricLine line : section.lines()) {
                String text = (line.text() != null) ? line.text() : "♪ ♪ ♪";
                allLinesText.add(text);
                if (line.time() != null) {
                    timedLines.add(new TimedLyric(line.time().total(), globalIndex.get()));
                }
                globalIndex.getAndIncrement();
            }
            allLinesText.add("");
            globalIndex.getAndIncrement();
        }
    }

    private void beginScheduling() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        long currentTimeMillis = System.currentTimeMillis();


        double elapsedTimeSeconds = (currentTimeMillis - this.songStartTimeMillis) / 1000.0;


        int initialHighlightIndex = -1;
        for (TimedLyric timedLine : timedLines) {
            if (timedLine.timestamp() <= elapsedTimeSeconds) {
                initialHighlightIndex = timedLine.globalIndex();
            } else {
                break;
            }
        }
        message.editMessageEmbeds(generateEmbedForLine(initialHighlightIndex)).queue();
        for (TimedLyric timedLine : timedLines) {
            long lyricTargetMillis = (long) (timedLine.timestamp() * 1000);
            long delayMillis = (this.songStartTimeMillis + lyricTargetMillis) - currentTimeMillis;
            if (delayMillis > 0) {
                scheduler.schedule(() -> message.editMessageEmbeds(generateEmbedForLine(timedLine.globalIndex())).queue(null, (error) -> performStop()), delayMillis, TimeUnit.MILLISECONDS);
            }
        }

    }

    private MessageEmbed generateEmbedForLine(int highlightedIndex) {
        EmbedBuilder builder = new EmbedBuilder();
        String currentTitle = this.title;

        if (this.isPaused) {
            currentTitle += " [PAUSED]";
        }
        builder.setTitle(currentTitle + " by " + this.author);
        builder.setFooter(this.source);
        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < allLinesText.size(); i++) {
            if (i == highlightedIndex) {
                contentBuilder.append("**").append(allLinesText.get(i)).append("**").append("\n");
            } else {
                contentBuilder.append(allLinesText.get(i)).append("\n");
            }
        }
        final int MAX_FIELD_LENGTH = 1023;
        final String INVISIBLE_SEPARATOR = "\u200B";
        String fullContent = contentBuilder.toString();
        if (fullContent.length() <= MAX_FIELD_LENGTH) {
            builder.setDescription(fullContent);
        } else {
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