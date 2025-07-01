package io.lolyay.customevents.events.commands;

import io.lolyay.customevents.Event;

public class CommandsRegistredEvent extends Event {

    private boolean syncedWithDiscord = false;

    public CommandsRegistredEvent(boolean syncedWithDiscord) {
        this.syncedWithDiscord = syncedWithDiscord;
    }

    public boolean isSyncedWithDiscord() {
        return syncedWithDiscord;
    }
}
