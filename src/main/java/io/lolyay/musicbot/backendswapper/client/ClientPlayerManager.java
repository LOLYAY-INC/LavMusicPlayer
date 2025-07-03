package io.lolyay.musicbot.backendswapper.client;

import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.player.Track;
import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.config.guildconfig.GuildConfigManager;
import io.lolyay.customevents.events.music.TrackStartedEvent;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.RequestorData;
import io.lolyay.musicbot.backendswapper.AbstractPlayerManager;
import io.lolyay.musicbot.backendswapper.client.search.ClientSearchManager;
import io.lolyay.musicbot.lyrics.live.SyncedLyricsPlayer;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ClientPlayerManager extends AbstractPlayerManager {
    private final Map<Long, GuildMusicManager> musicManagers;
    public final LavalinkClient lavaLinkClient;

    public ClientPlayerManager(LavalinkClient lavaLinkClient) {
        this.lavaLinkClient = lavaLinkClient;
        this.musicManagers = new ConcurrentHashMap<>();
    }

    @Override
    public void searchWithDefaultOrder(String query, Optional<Member> member, Consumer<Search> callback, long guildId) {
        new ClientSearchManager(guildId).searchWithDefaultOrder(query, member, callback, guildId);
    }

    @Override
    public synchronized GuildMusicManager getGuildMusicManager(long guildId) {
        return musicManagers.computeIfAbsent(guildId, (key) -> new GuildMusicManager(this, key, GuildConfigManager.getGuildConfig(Long.toString(key))));
    }

    @Override
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

    @Override
    public void playTrack(MusicAudioTrack track) {
        long guildId = track.userData().dcGuildId();
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setTrack(track.track())
                .setVolume((int) getGuildMusicManager(guildId).getVolume())
                .subscribe((e) -> {
                    JdaMain.eventBus.post(new TrackStartedEvent(track, guildId, lavaLinkClient.getOrCreateLink(guildId).getNode()));
                    track.startTime(System.currentTimeMillis() - Integer.parseInt(ConfigManager.getConfig("live-lyrics-ping-compensation")));
                    Logger.debug("Set StartTime for track of guild " + guildId + " to " + track.startTime());
                    if (SyncedLyricsPlayer.isLive(guildId)) {
                        SyncedLyricsPlayer.nextSong(guildId, track.trackInfo().title(), track.startTime());
                        Logger.debug("Next song for lyrics player of guild " + guildId + " set!");
                    }
                });
    }

    @Override
    public void stop(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setTrack(null)
                .subscribe(e -> SyncedLyricsPlayer.stop(guildId));
        disconnect(JdaMain.jda.getGuildById(guildId));

    }

    @Override
    public void pause(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setPaused(true)
                .subscribe(e -> SyncedLyricsPlayer.setPaused(guildId, true, System.currentTimeMillis()));

    }

    @Override
    public void resume(long guildId) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setPaused(false)
                .subscribe(e -> SyncedLyricsPlayer.setPaused(guildId, false, System.currentTimeMillis()));

    }

    @Override
    public void setVolume(long guildId, int volume) {
        lavaLinkClient.getOrCreateLink(guildId).createOrUpdatePlayer()
                .setVolume(volume)
                .subscribe();
    }

    @Override
    public void connect(AudioChannel voiceChannel) {
        JdaMain.jda.getDirectAudioController().connect(voiceChannel);
    }

    @Override
    public void disconnect(Guild voiceChannel) {
        JdaMain.jda.getDirectAudioController().disconnect(voiceChannel);
    }
}
