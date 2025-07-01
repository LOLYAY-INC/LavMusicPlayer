package io.lolyay.customevents.events;

import io.lolyay.customevents.Event;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

public class MusicStartedEvent extends Event {

    private final MusicAudioTrack track;

    public MusicStartedEvent(MusicAudioTrack track) {
        this.track = track;
    }

    public MusicAudioTrack getTrack() {
        return track;
    }
}
