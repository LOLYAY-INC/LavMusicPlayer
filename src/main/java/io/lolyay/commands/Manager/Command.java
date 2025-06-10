package io.lolyay.commands.Manager;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface Command {
    public String getName();
    public String getDescription();

    public CommandOptionType[] getOptions();
    public default boolean requiresPermission(){
        return true;
    }

    public void execute(SlashCommandInteractionEvent event);

}
