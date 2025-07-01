package io.lolyay.events;

import io.lolyay.customevents.EventBus;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class JdaEventsToBusEvents implements EventListener {

    private final EventBus eventBus;

    public JdaEventsToBusEvents(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        eventBus.postJda(genericEvent);
    }
}
