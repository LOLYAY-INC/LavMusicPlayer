package io.lolyay.customevents.events.lifecycle;

import io.lolyay.customevents.Event;
import net.dv8tion.jda.api.JDA;

public class BotReadyEvent extends Event {
    private final JDA jda;

    public BotReadyEvent(JDA jda) {
        this.jda = jda;
    }

    public JDA getBuilder() {
        return jda;
    }
}
