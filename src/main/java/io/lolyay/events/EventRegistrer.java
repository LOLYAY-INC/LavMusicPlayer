package io.lolyay.events;

import io.lolyay.JdaMain;

public class EventRegistrer {
    public static void register() {
        JdaMain.eventBus.register(new OnReadyEventListener());
        JdaMain.eventBus.register(new OnSlashCommandInteractionEventListener());
        JdaMain.eventBus.register(new OnGuildVoiceUpdate());
        JdaMain.eventBus.register(new OnTrackEnd());

    }
}
