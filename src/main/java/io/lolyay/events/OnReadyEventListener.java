package io.lolyay.events;

import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnReadyEventListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Logger.log("Bot connected to Discord!");
    }
}
