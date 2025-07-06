package io.lolyay.musicbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.customevents.events.music.TrackStartedEvent;
import io.lolyay.musicbot.abstracts.AbstractPlayerManager;
import io.lolyay.musicbot.output.Player;
import io.lolyay.musicbot.search.LavaSearchManager;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class LavaLinkPlayerManager extends AbstractPlayerManager {
    private final AudioPlayerManager audioPlayerManager;
    private final LavaPlayerFactory playerFactory;

    public LavaLinkPlayerManager(AudioPlayerManager audioPlayerManager, LavaPlayerFactory playerFactory) {
        this.audioPlayerManager = audioPlayerManager;
        this.playerFactory = playerFactory;
    }

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    public LavaPlayerFactory getPlayerFactory() {
        return playerFactory;
    }

    @Override
    public void searchWithDefaultOrder(String query, Consumer<Search> callback) {
        new LavaSearchManager().searchWithDefaultOrder(query, callback);
    }

    public void searchWithDefaultOrderMultiple(String query, Consumer<Search[]> callback) {
        new LavaSearchManager().searchWithDefaultOrderMultiple(query, callback);
    }


    @Override
    public void playTrack(MusicAudioTrack track) {
        getPlayerFactory().getOrCreatePlayer().playTrack(track.audioTrack().makeClone());
        JdaMain.eventBus.post(new TrackStartedEvent(track, -1 ,null));
        track.startTime(System.currentTimeMillis());
        Player.INSTANCE.startSending();
        JdaMain.mediaManager.started(track);
        Logger.debug("Started playing track: " + track.audioTrack().getInfo().title);
    }

    @Override
    public void stop() {
        getPlayerFactory().getOrCreatePlayer().stopTrack();
        Player.INSTANCE.stopSending();
        JdaMain.mediaManager.stopped();
    }

    @Override
    public void pause() {
        getPlayerFactory().getOrCreatePlayer().setPaused(true);
        JdaMain.mediaManager.paused();
    }

    @Override
    public void resume() {
        getPlayerFactory().getOrCreatePlayer().setPaused(false);
        JdaMain.mediaManager.started(new MusicAudioTrack(playerFactory.getOrCreatePlayer().getPlayingTrack()));
    }

    @Override
    public void setVolume(int volume) {
        getPlayerFactory().getOrCreatePlayer().setVolume(volume);
    }

}
