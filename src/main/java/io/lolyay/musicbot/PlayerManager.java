package io.lolyay.musicbot;

import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.event.TrackEndEvent;
import dev.arbjerg.lavalink.client.player.Track;
import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.config.guildconfig.GuildConfigManager;
import io.lolyay.customevents.events.music.TrackEndedEvent;
import io.lolyay.musicbot.lyrics.live.SyncedLyricsPlayer;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.entities.Member;

import java.net.URI;
import java.sql.Timestamp;
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

            JdaMain.eventBus.post(new TrackEndedEvent(musicManager.getQueue().getFirst(), event.getEndReason(), event.getGuildId(), event.getNode()));

            if (event.getEndReason().getMayStartNext())
                musicManager.onTrackEnd();
            else
                musicManager.getQueManager().clear();

        });
    }

    public synchronized GuildMusicManager getGuildMusicManager(long guildId) {
        return musicManagers.computeIfAbsent(guildId, id -> new GuildMusicManager(this, id, GuildConfigManager.getGuildConfig(id.toString())));
    }


    @Deprecated
    public void searchTrack(String query, Member member, Consumer<MusicAudioTrack> successCallback, Runnable failureCallback, Runnable notFoundCallback) {
    }

    @Deprecated
    private void searchTrackWithYoutube(String query, Member member, Consumer<MusicAudioTrack> successCallback, Runnable failureCallback, Runnable notFoundCallback) {

    }

    @Deprecated
    private void searchTrackWithYoutubeMusic(String query, Member member, Consumer<MusicAudioTrack> successCallback, Runnable failureCallback, Runnable notFoundCallback) {

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
                    track.startTime(new Timestamp(track.startTime().getTime() - Integer.parseInt(ConfigManager.getConfig("live-lyrics-ping-compensation"))));
                    if (SyncedLyricsPlayer.isLive(guildId)) {
                        SyncedLyricsPlayer.nextSong(guildId, track.track().getInfo().getTitle(), track.startTime().getTime());
                        Logger.debug("Next song for lyrics!");
                    }
                });
    }

    public void stop(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setTrack(null)
                .subscribe(e -> SyncedLyricsPlayer.stop(guildId));
        JdaMain.jda.getDirectAudioController().disconnect(JdaMain.jda.getGuildById(guildId));

    }

    public void pause(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setPaused(true)
                .subscribe(e -> SyncedLyricsPlayer.setPaused(guildId, true, System.currentTimeMillis()));

    }

    public void resume(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setPaused(false)
                .subscribe(e -> SyncedLyricsPlayer.setPaused(guildId, false, System.currentTimeMillis()));

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