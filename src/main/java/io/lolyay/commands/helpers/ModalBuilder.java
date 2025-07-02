package io.lolyay.commands.helpers;

import net.dv8tion.jda.api.requests.restaction.interactions.ModalCallbackAction;

public class ModalBuilder {
    private final ModalCallbackAction action;

    public ModalBuilder(ModalCallbackAction action) {
        this.action = action;
    }

    public void queue() {
        if (action == null) return;
        action.queue();
    }

    public Void complete() {
        if (action == null) return null;
        return action.complete();
    }
}
