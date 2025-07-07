package io.lolyay.eventlisteners;


import io.lolyay.LavMusicPlayer;
import io.lolyay.eventlisteners.listeners.MediaEventListener;
import io.lolyay.eventlisteners.listeners.TrackEventListener;

public class EventRegistrer {
    public static void register() {
        LavMusicPlayer.eventBus.register(new MediaEventListener());
        LavMusicPlayer.eventBus.register(new TrackEventListener());


    }
}
