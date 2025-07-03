package io.lolyay.musicbot.backendswapper.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.config.guildconfig.GuildConfigManager;
import io.lolyay.customevents.events.music.TrackStartedEvent;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.backendswapper.AbstractPlayerManager;
import io.lolyay.musicbot.backendswapper.lavaplayer.search.LavaSearchManager;
import io.lolyay.musicbot.lyrics.live.SyncedLyricsPlayer;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class LavaLinkPlayerManager extends AbstractPlayerManager {
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;
    private final LavaPlayerFactory playerFactory;

    public LavaLinkPlayerManager(AudioPlayerManager audioPlayerManager, LavaPlayerFactory playerFactory) {
        this.audioPlayerManager = audioPlayerManager;
        this.playerFactory = playerFactory;
        this.musicManagers = new ConcurrentHashMap<>();
    }

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    public LavaPlayerFactory getPlayerFactory() {
        return playerFactory;
    }

    @Override
    public void searchWithDefaultOrder(String query, Optional<Member> member, Consumer<Search> callback, long guildId) {
        new LavaSearchManager(guildId).searchWithDefaultOrder(query, member, callback, guildId);
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


    @Override
    public void playTrack(MusicAudioTrack track) {
        long guildId = track.userData().dcGuildId();
        getPlayerFactory().getOrCreatePlayer(guildId).playTrack(track.audioTrack().makeClone());
        JdaMain.eventBus.post(new TrackStartedEvent(track, guildId, null));
        track.startTime(System.currentTimeMillis() - Integer.parseInt(ConfigManager.getConfig("live-lyrics-ping-compensation")));
        Logger.debug("Set StartTime for track of guild " + guildId + " to " + track.startTime());
        if (SyncedLyricsPlayer.isLive(guildId)) {
            SyncedLyricsPlayer.nextSong(guildId, track.trackInfo().title(), track.startTime());
            Logger.debug("Next song for lyrics player of guild " + guildId + " set!");
        }
        getPlayerFactory().getOrCreatePlayer(guildId).setVolume((int) getGuildMusicManager(guildId).getVolume());

    }

    @Override
    public void stop(long guildId) {
        getPlayerFactory().getOrCreatePlayer(guildId).stopTrack();
        JdaMain.jda.getDirectAudioController().disconnect(JdaMain.jda.getGuildById(guildId));

    }

    @Override
    public void pause(long guildId) {
        getPlayerFactory().getOrCreatePlayer(guildId).setPaused(true);
        SyncedLyricsPlayer.setPaused(guildId, true, System.currentTimeMillis());
    }

    @Override
    public void resume(long guildId) {
        getPlayerFactory().getOrCreatePlayer(guildId).setPaused(false);
        SyncedLyricsPlayer.setPaused(guildId, false, System.currentTimeMillis());

    }

    @Override
    public void setVolume(long guildId, int volume) {
        getPlayerFactory().getOrCreatePlayer(guildId).setVolume(volume);
    }


    @Override
    public void connect(AudioChannel voiceChannel) {

        voiceChannel.getGuild().getAudioManager().setSendingHandler(new LavaAudioSendHandler(
                getPlayerFactory().getOrCreatePlayer(voiceChannel.getGuild().getIdLong())
        ));
        voiceChannel.getGuild().getAudioManager().openAudioConnection(voiceChannel);
    }

    @Override
    public void disconnect(Guild voiceChannel) {
        voiceChannel.getAudioManager().closeAudioConnection();
    }
}
