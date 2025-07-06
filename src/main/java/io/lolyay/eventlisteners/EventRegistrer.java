package io.lolyay.eventlisteners;


import io.lolyay.JdaMain;
import io.lolyay.eventlisteners.listeners.MediaEventListener;

public class EventRegistrer {
    public static void register() {
        JdaMain.eventBus.register(new MediaEventListener());


    }
}
