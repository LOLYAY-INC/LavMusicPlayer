package io.lolyay.musicbot;

import dev.arbjerg.lavalink.client.FunctionalLoadResultHandler;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.event.TrackEndEvent;
import dev.arbjerg.lavalink.client.player.Track;
import io.lolyay.JdaMain;
import io.lolyay.config.guildconfig.GuildConfigManager;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.entities.Member;

import java.net.URI;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
        return musicManagers.computeIfAbsent(guildId, id -> new GuildMusicManager(this, id, GuildConfigManager.getGuildConfig(id.toString())));
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


        final String finalQuery = query;

        lavaLinkClient.getOrCreateLink(guildId)
                .loadItem(finalQuery)
                .subscribe(new FunctionalLoadResultHandler(

                        trackLoaded -> {
                            Logger.log("Loaded single track: " + trackLoaded.getTrack().getInfo().getTitle());
                            MusicAudioTrack track = createMusicAudioTrack(trackLoaded.getTrack(), member);
                            successCallback.accept(track);
                        },

                        playlistLoaded -> {


                            Track firstTrack = playlistLoaded.getTracks().getFirst();
                            if (firstTrack != null) {
                                Logger.log("Loaded playlist, taking first track: " + firstTrack.getInfo().getTitle());
                                MusicAudioTrack track = createMusicAudioTrack(firstTrack, member);
                                successCallback.accept(track);
                            } else {
                                failureCallback.run();
                            }
                        },

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

                        () -> searchTrackWithYoutubeMusic(finalQuery,member,successCallback,failureCallback),

                        exception -> {
                            Logger.err("Error loading track: " + exception.getException().getMessage());
                            failureCallback.run();
                        }
                ));
    }

    private void searchTrackWithYoutube(String query, Member member, Consumer<MusicAudioTrack> successCallback, Runnable failureCallback) {
        long guildId = member.getGuild().getIdLong();


        final String finalQuery = "ytsearch:" +  query;
        Logger.debug("Couldn't Locate track on Youtube Music, Searching with youtube: " + query);

        lavaLinkClient.getOrCreateLink(guildId)
                .loadItem(finalQuery)
                .subscribe(new FunctionalLoadResultHandler(

                        trackLoaded -> {
                            Logger.log("Loaded single track: " + trackLoaded.getTrack().getInfo().getTitle());
                            MusicAudioTrack track = createMusicAudioTrack(trackLoaded.getTrack(), member);
                            successCallback.accept(track);
                        },

                        playlistLoaded -> {


                            Track firstTrack = playlistLoaded.getTracks().getFirst();
                            if (firstTrack != null) {
                                Logger.log("Loaded playlist, taking first track: " + firstTrack.getInfo().getTitle());
                                MusicAudioTrack track = createMusicAudioTrack(firstTrack, member);
                                successCallback.accept(track);
                            } else {
                                failureCallback.run();
                            }
                        },

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

                        failureCallback,

                        exception -> {
                            Logger.log("Error loading track: " + exception.getException().getMessage());
                            failureCallback.run();
                        }
                ));
    }

    private void searchTrackWithYoutubeMusic(String query, Member member, Consumer<MusicAudioTrack> successCallback, Runnable failureCallback) {
        long guildId = member.getGuild().getIdLong();


        final String finalQuery = "ytmsearch:" +  query;
        Logger.debug("Couldn't normally Locate track, Searching with youtube music: " + query);

        lavaLinkClient.getOrCreateLink(guildId)
                .loadItem(finalQuery)
                .subscribe(new FunctionalLoadResultHandler(

                        trackLoaded -> {
                            Logger.log("Loaded single track: " + trackLoaded.getTrack().getInfo().getTitle());
                            MusicAudioTrack track = createMusicAudioTrack(trackLoaded.getTrack(), member);
                            successCallback.accept(track);
                        },

                        playlistLoaded -> {


                            Track firstTrack = playlistLoaded.getTracks().getFirst();
                            if (firstTrack != null) {
                                Logger.log("Loaded playlist, taking first track: " + firstTrack.getInfo().getTitle());
                                MusicAudioTrack track = createMusicAudioTrack(firstTrack, member);
                                successCallback.accept(track);
                            } else {
                                failureCallback.run();
                            }
                        },

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

                        () -> searchTrackWithYoutube(finalQuery,member,successCallback,failureCallback),

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


    public void playTrack(MusicAudioTrack track) {
        long guildId = track.userData().dcGuildId();
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setTrack(track.track())
                .setVolume((int) getGuildMusicManager(guildId).getVolume())
                .subscribe((e) -> {
                    track.startTime(new Timestamp(System.currentTimeMillis()));
                });
    }

    public void stop(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setTrack(null)
                .subscribe();
        JdaMain.jda.getDirectAudioController().disconnect(JdaMain.jda.getGuildById(guildId));

    }

    public void pause(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setPaused(true)
                .subscribe();

    }

    public void resume(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setPaused(false)
                .subscribe();

    }

    public void setVolume(long guildId, int volume) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setVolume(volume)
                .subscribe();
    }

    /**
     * Gets the current position of the currently playing track in milliseconds.
     *
     * @param guildId The ID of the guild to get the position for
     * @return A CompletableFuture that will be completed with the current position in milliseconds,
     *         or -1 if no track is playing or an error occurs
     */
    public CompletableFuture<Long> getPosition(long guildId) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        
        lavaLinkClient.getOrCreateLink(guildId).getPlayer().subscribe(
            player -> {
                try {
                    if (player != null && player.getTrack() != null) {
                        future.complete(player.getTrack().getInfo().getPosition());
                    } else {
                        future.complete(-1L);
                    }
                } catch (Exception e) {
                    Logger.err("Error getting track position: " + e.getMessage());
                    future.complete(-1L);
                }
            },
            error -> {
                Logger.err("Error getting player: " + error.getMessage());
                future.complete(-1L);
            }
        );
        
        return future;
    }
}