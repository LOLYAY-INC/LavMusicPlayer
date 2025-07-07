package io.lolyay.eventlisteners;


import io.lolyay.LavMusicPlayer;
import io.lolyay.eventlisteners.listeners.MediaEventListener;

public class EventRegistrer {
    public static void register() {
        LavMusicPlayer.eventBus.register(new MediaEventListener());


    }
}
