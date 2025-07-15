package io.lolyay.music.lavalink;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.lolyay.LavMusicPlayer;
import io.lolyay.events.music.TrackPausedEvent;
import io.lolyay.events.music.TrackStartedEvent;
import io.lolyay.events.volume.VolumeChangedEvent;
import io.lolyay.music.abstracts.AbstractPlayerManager;
import io.lolyay.music.output.OpenAlPlayer;
import io.lolyay.music.output.Pcm16Player;
import io.lolyay.search.LavaSearchManager;
import io.lolyay.search.Search;
import io.lolyay.music.track.MusicAudioTrack;
import io.lolyay.utils.Logger;

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

    @Deprecated
    @Override
    public void searchWithDefaultOrder(String query, Consumer<Search> callback) {
        new LavaSearchManager().searchWithDefaultOrder(query, callback);
    }

    public void searchWithDefaultOrderMultiple(String query, Consumer<Search[]> callback) {
        new LavaSearchManager().searchWithDefaultOrderMultiple(query, callback);
    }


    @Override
    public void playTrack(MusicAudioTrack track) {
        LavaPlayerFactory.player.playTrack(track.audioTrack().makeClone());

        LavMusicPlayer.eventBus.post(new TrackStartedEvent(track, LavaPlayerFactory.player));

        track.startTime(System.currentTimeMillis());

        OpenAlPlayer.INSTANCE.startSending();

        Logger.debug("Started playing track: " + track.audioTrack().getInfo().title);
    }

    @Override
    public void stop() {
        getPlayerFactory().getOrCreatePlayer().stopTrack();

        OpenAlPlayer.INSTANCE.stopSending();
    }

    @Override
    public void pause() {
        getPlayerFactory().getOrCreatePlayer().setPaused(true);
        LavMusicPlayer.eventBus.post(new TrackPausedEvent(LavMusicPlayer.musicManager.getCurrentTrack(), LavaPlayerFactory.player));
    }

    @Override
    public void resume() {
        getPlayerFactory().getOrCreatePlayer().setPaused(false);
        LavMusicPlayer.eventBus.post(new TrackStartedEvent(LavMusicPlayer.musicManager.getCurrentTrack(), LavaPlayerFactory.player));
    }

    @Override
    public void setVolume(int volume) {
        if(LavMusicPlayer.eventBus.postAndGet(new VolumeChangedEvent(((Long) LavMusicPlayer.musicManager.getVolume()).intValue(), volume, LavaPlayerFactory.player, LavMusicPlayer.musicManager.getCurrentTrack())).isCancelled())
            return;
        getPlayerFactory().getOrCreatePlayer().setVolume(volume);
    }

}
