package io.lolyay.eventbus.events.volume;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.lolyay.eventbus.CancellableEvent;
import io.lolyay.eventbus.Event;
import io.lolyay.music.track.MusicAudioTrack;

public class VolumeChangedEvent extends CancellableEvent {
    private final int previousVolume;
    private final int newVolume;
    private final AudioPlayer player;
    private final MusicAudioTrack track;

    public VolumeChangedEvent(int previousVolume, int newVolume, AudioPlayer player, MusicAudioTrack track) {
        this.previousVolume = previousVolume;
        this.newVolume = newVolume;
        this.player = player;
        this.track = track;
    }

    public int getPreviousVolume() {
        return previousVolume;
    }

    public int getNewVolume() {
        return newVolume;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public MusicAudioTrack getTrack() {
        return track;
    }
}
