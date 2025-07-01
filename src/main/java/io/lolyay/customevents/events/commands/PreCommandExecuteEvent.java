package io.lolyay.customevents.events.commands;

import io.lolyay.commands.manager.Command;
import io.lolyay.customevents.CancellableEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public class PreCommandExecuteEvent extends CancellableEvent {

    private final Command command;
    private final SlashCommandInteractionEvent interactionEvent;
    private final Member commandExecutor;
    private final Guild guild;

    public PreCommandExecuteEvent(Command command, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        this.command = command;
        this.interactionEvent = slashCommandInteractionEvent;
        this.commandExecutor = slashCommandInteractionEvent.getMember();
        this.guild = slashCommandInteractionEvent.getGuild();
    }

    public SlashCommandInteractionEvent getInteractionEvent() {
        return interactionEvent;
    }

    public Command getCommand() {
        return command;
    }

    public Member getCommandExecutor() {
        return commandExecutor;
    }

    public Guild getGuild() {
        return guild;
    }
}
