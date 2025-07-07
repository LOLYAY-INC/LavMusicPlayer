package io.lolyay.music.lavalink;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import io.lolyay.LavMusicPlayer;
import io.lolyay.eventbus.events.music.TrackEndedEvent;
import io.lolyay.music.MusicManager;
import io.lolyay.music.structs.MusicTrackEndReason;

public class LavaEventTranslator extends AudioEventAdapter {
    @Override
    public void onPlayerPause(AudioPlayer player) {


    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        MusicManager musicManager = MusicManager.getInstance();

        LavMusicPlayer.eventBus.post(new TrackEndedEvent(musicManager.getCurrentTrack(), MusicTrackEndReason.fromAudioTrackEndReason(endReason), player));

        if (endReason.mayStartNext && !LavMusicPlayer.headlessMode.isHeadless) {
            musicManager.onTrackEnd();
        }

    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        // An already playing track threw an exception (track end event will still be received separately)
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
    }
}
