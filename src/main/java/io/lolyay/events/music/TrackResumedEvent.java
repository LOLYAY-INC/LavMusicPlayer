package io.lolyay.events.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.lolyay.eventbus.Event;
import io.lolyay.music.track.MusicAudioTrack;

public class TrackResumedEvent extends Event {
    private final MusicAudioTrack track;
    private final AudioPlayer player;

    public TrackResumedEvent(MusicAudioTrack track, AudioPlayer player) {
        this.track = track;
        this.player = player;
    }


    public MusicAudioTrack getTrack() {
        return track;
    }

    public AudioPlayer getPlayer() {
        return player;
    }
}
