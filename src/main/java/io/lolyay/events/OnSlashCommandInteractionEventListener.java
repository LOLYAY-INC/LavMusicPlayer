package io.lolyay.events;


import io.lolyay.commands.manager.CommandRegistrer;
import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnSlashCommandInteractionEventListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CommandRegistrer.runCommand(event.getName(), event);
        Logger.log("Executing (Slash) command: " + event.getName());
    }
}