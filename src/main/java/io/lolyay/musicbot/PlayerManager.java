package io.lolyay.musicbot;

import dev.arbjerg.lavalink.client.FunctionalLoadResultHandler;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.event.TrackEndEvent;
import dev.arbjerg.lavalink.client.player.Track;
import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.entities.Member;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class PlayerManager {
    private final LavalinkClient lavaLinkClient;
    private final Map<Long, GuildMusicManager> musicManagers = new ConcurrentHashMap<>();

    public PlayerManager(LavalinkClient lavaLinkClient) {
        this.lavaLinkClient = lavaLinkClient;
        this.setupEventListeners();
    }

    private void setupEventListeners() {
        lavaLinkClient.on(TrackEndEvent.class).subscribe(event -> {
            GuildMusicManager musicManager = musicManagers.get(event.getGuildId());
            if (musicManager != null && event.getEndReason().getMayStartNext()) {
                musicManager.onTrackEnd();
            } else {
                assert musicManager != null;
                musicManager.getQueManager().clear();
            }
        });
    }

    public synchronized GuildMusicManager getGuildMusicManager(long guildId) {
        return musicManagers.computeIfAbsent(guildId, id -> new GuildMusicManager(this, id, Long.parseLong(ConfigManager.getConfig("default-volume"))));
    }

    /**
     * Asynchronously searches for a track.
     * This method does not return a track directly. Instead, it accepts callbacks
     * that will be executed upon completion.
     *
     * @param query           The search query or URL.
     * @param member          The member who requested the track.
     * @param successCallback A function to be called when a track is successfully found.
     * @param failureCallback A function to be called when no track is found or an error occurs.
     */
    public void searchTrack(String query, Member member, Consumer<MusicAudioTrack> successCallback, Runnable failureCallback) {
        long guildId = member.getGuild().getIdLong();

        // If the query isn't a URL, prefix it with "ytsearch:" to ensure a YouTube search.
        final String finalQuery = query;

        lavaLinkClient.getOrCreateLink(guildId)
                .loadItem(finalQuery)
                .subscribe(new FunctionalLoadResultHandler(
                        // A single track was loaded (e.g., from a direct URL)
                        trackLoaded -> {
                            Logger.log("Loaded single track: " + trackLoaded.getTrack().getInfo().getTitle());
                            MusicAudioTrack track = createMusicAudioTrack(trackLoaded.getTrack(), member);
                            successCallback.accept(track);
                        },
                        // A playlist was loaded
                        playlistLoaded -> {
                            // For this example, we'll just take the first track of the playlist.
                            // In a real bot, you might want to queue all tracks.
                            Track firstTrack = playlistLoaded.getTracks().getFirst();
                            if (firstTrack != null) {
                                Logger.log("Loaded playlist, taking first track: " + firstTrack.getInfo().getTitle());
                                MusicAudioTrack track = createMusicAudioTrack(firstTrack, member);
                                successCallback.accept(track);
                            } else {
                                failureCallback.run();
                            }
                        },
                        // A search result was loaded
                        searchResult -> {
                            final List<Track> tracks = searchResult.getTracks();
                            if (tracks.isEmpty()) {
                                Logger.warn("Search returned no results for: " + finalQuery);
                                failureCallback.run();
                                return;
                            }
                            final Track firstTrack = tracks.getFirst();
                            Logger.log("Loaded from search: " + firstTrack.getInfo().getTitle());
                            MusicAudioTrack track = createMusicAudioTrack(firstTrack, member);
                            successCallback.accept(track);
                        },
                        // No matches were found for the query
                        () -> searchTrackWithYoutube(finalQuery,member,successCallback,failureCallback),
                        // The load failed with an exception
                        exception -> {
                            Logger.err("Error loading track: " + exception.getException().getMessage());
                            failureCallback.run();
                        }
                ));
    }

    private void searchTrackWithYoutube(String query, Member member, Consumer<MusicAudioTrack> successCallback, Runnable failureCallback) {
        long guildId = member.getGuild().getIdLong();

        // If the query isn't a URL, prefix it with "ytsearch:" to ensure a YouTube search.
        final String finalQuery = "ytsearch:" +  query;
        Logger.debug("Couldnt Locate track, searching with youtube: " + query);

        lavaLinkClient.getOrCreateLink(guildId)
                .loadItem(finalQuery)
                .subscribe(new FunctionalLoadResultHandler(
                        // A single track was loaded (e.g., from a direct URL)
                        trackLoaded -> {
                            Logger.log("Loaded single track: " + trackLoaded.getTrack().getInfo().getTitle());
                            MusicAudioTrack track = createMusicAudioTrack(trackLoaded.getTrack(), member);
                            successCallback.accept(track);
                        },
                        // A playlist was loaded
                        playlistLoaded -> {
                            // For this example, we'll just take the first track of the playlist.
                            // In a real bot, you might want to queue all tracks.
                            Track firstTrack = playlistLoaded.getTracks().getFirst();
                            if (firstTrack != null) {
                                Logger.log("Loaded playlist, taking first track: " + firstTrack.getInfo().getTitle());
                                MusicAudioTrack track = createMusicAudioTrack(firstTrack, member);
                                successCallback.accept(track);
                            } else {
                                failureCallback.run();
                            }
                        },
                        // A search result was loaded
                        searchResult -> {
                            final List<Track> tracks = searchResult.getTracks();
                            if (tracks.isEmpty()) {
                                Logger.warn("Search returned no results for: " + finalQuery);
                                failureCallback.run();
                                return;
                            }
                            final Track firstTrack = tracks.getFirst();
                            Logger.log("Loaded from search: " + firstTrack.getInfo().getTitle());
                            MusicAudioTrack track = createMusicAudioTrack(firstTrack, member);
                            successCallback.accept(track);
                        },
                        // No matches were found for the query
                        failureCallback,
                        // The load failed with an exception
                        exception -> {
                            Logger.log("Error loading track: " + exception.getException().getMessage());
                            failureCallback.run();
                        }
                ));
    }

    /**
     * Helper method to create a MusicAudioTrack and set its user data.
     * @param track The Lavalink track.
     * @param member The member who requested it.
     * @return A new MusicAudioTrack.
     */
    private MusicAudioTrack createMusicAudioTrack(Track track, Member member) {
        final RequestorData userData = new RequestorData(member.getIdLong(), member.getEffectiveName(), member.getGuild().getIdLong());
        track.setUserData(userData);
        return new MusicAudioTrack(track, userData);
    }

    /**
     * Simple utility to check if a string is a valid URL.
     * @param input The string to check.
     * @return true if it is a URL, false otherwise.
     */
    private boolean isUrl(String input) {
        try {
            new URI(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- Player Control Methods ---

    public void playTrack(MusicAudioTrack track) {
        long guildId = track.userData().dcGuildId();
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setTrack(track.track())
                .setVolume((int) getGuildMusicManager(guildId).getVolume())
                .subscribe();
    }

    public void stop(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setTrack(null)
                .subscribe();
        JdaMain.jda.getDirectAudioController().disconnect(JdaMain.jda.getGuildById(guildId));
       // lavaLinkClient.getOrCreateLink(guildId).destroy();
    }

    public void pause(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setPaused(true)
                .subscribe();
        // lavaLinkClient.getOrCreateLink(guildId).destroy();
    }

    public void resume(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setPaused(false)
                .subscribe();
        // lavaLinkClient.getOrCreateLink(guildId).destroy();
    }

    public void setVolume(long guildId, int volume) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setVolume(volume)
                .subscribe();
    }
}