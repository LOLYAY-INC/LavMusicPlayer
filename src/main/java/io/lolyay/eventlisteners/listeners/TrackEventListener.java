package io.lolyay.eventlisteners.listeners;

import io.lolyay.LavMusicPlayer;
import io.lolyay.eventbus.EventListener;
import io.lolyay.eventbus.events.music.TrackEndedEvent;
import io.lolyay.eventbus.events.music.TrackPausedEvent;
import io.lolyay.eventbus.events.music.TrackResumedEvent;
import io.lolyay.eventbus.events.music.TrackStartedEvent;

public class TrackEventListener {
    @EventListener
    public void onTrackStarted(TrackStartedEvent event) {
        LavMusicPlayer.mediaManager.started(event.getTrack());
    }

    @EventListener
    public void onTrackStopped(TrackEndedEvent event) {
        LavMusicPlayer.mediaManager.stopped();
    }

    @EventListener
    public void onTrackPaused(TrackPausedEvent event) {
        LavMusicPlayer.mediaManager.paused();
    }

    @EventListener
    public void onTrackResumed(TrackResumedEvent event) {
        LavMusicPlayer.mediaManager.started(event.getTrack());
    }
}
