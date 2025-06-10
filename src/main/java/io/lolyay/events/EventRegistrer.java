package io.lolyay.events;

import io.lolyay.JdaMain;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventRegistrer {
    public static void register() {
        registerImpl(new OnReadyEventListener());
        registerImpl(new OnSlashCommandInteractionEventListener());

    }



    private static <T extends ListenerAdapter> void registerImpl(T listener) {
        JdaMain.builder.addEventListeners(listener);

    }
}
