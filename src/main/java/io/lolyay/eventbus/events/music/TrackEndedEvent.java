package io.lolyay.eventbus.events.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.lolyay.eventbus.Event;
import io.lolyay.music.structs.MusicTrackEndReason;
import io.lolyay.music.track.MusicAudioTrack;

public class TrackEndedEvent extends Event {

    private final MusicAudioTrack track;
    private final MusicTrackEndReason endReason;
    private final AudioPlayer player;

    public TrackEndedEvent(MusicAudioTrack track, MusicTrackEndReason endReason, AudioPlayer player) {
        this.track = track;
        this.endReason = endReason;
        this.player = player;
    }

    public MusicAudioTrack getTrack() {
        return track;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public MusicTrackEndReason getEndReason() {
        return endReason;
    }


}
