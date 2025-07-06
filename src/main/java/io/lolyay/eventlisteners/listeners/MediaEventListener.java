package io.lolyay.eventlisteners.listeners;

import io.lolyay.JdaMain;
import io.lolyay.customevents.Event;
import io.lolyay.customevents.EventListener;
import io.lolyay.customevents.events.media.MediaPauseEvent;
import io.lolyay.customevents.events.media.MediaPlayEvent;
import io.lolyay.customevents.events.media.MediaStopEvent;
import io.lolyay.utils.Logger;

public class MediaEventListener extends Event {
    @EventListener
    public void onMediaPlay(MediaPlayEvent event) {
        Logger.debug("OS Play button pressed! Resuming music...");
        JdaMain.musicManager.resume();
    }

    @EventListener
    public void onMediaPause(MediaPauseEvent event) {
        Logger.debug("OS Pause button pressed! Pausing music...");
        // Here you would call your music player's pause logic
        JdaMain.musicManager.pause();

    }

    @EventListener
    public void onMediaStop(MediaStopEvent event) {
        Logger.debug("OS Stop button pressed! Stopping music...");
        // Here you would call your music player's stop logic
        JdaMain.musicManager.stop();

    }
}
