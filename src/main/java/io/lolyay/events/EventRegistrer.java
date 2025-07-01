package io.lolyay.events;

import io.lolyay.JdaMain;
import io.lolyay.events.listeners.jda.OnGuildVoiceUpdate;
import io.lolyay.events.listeners.jda.OnReadyEventListener;
import io.lolyay.events.listeners.jda.OnSlashCommandInteractionEventListener;
import io.lolyay.events.listeners.lavalink.OnTrackEnd;

public class EventRegistrer {
    public static void register() {
        JdaMain.eventBus.register(new OnReadyEventListener());
        JdaMain.eventBus.register(new OnSlashCommandInteractionEventListener());
        JdaMain.eventBus.register(new OnGuildVoiceUpdate());
        JdaMain.eventBus.register(new OnTrackEnd());

    }
}
