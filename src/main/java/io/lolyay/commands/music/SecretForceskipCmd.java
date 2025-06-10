
package io.lolyay.commands.music;

import io.lolyay.infusiadc.Bot;
import io.lolyay.infusiadc.Commands.Manager.CommandOptionType;
import io.lolyay.infusiadc.MusicBot.commands.SlashMusicCommandType;
import io.lolyay.infusiadc.Utils.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SecretForceskipCmd implements SlashMusicCommandType {


    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skips the current track and plays the next one in the queue.";
    }

    @Override
    public CommandOptionType[] getOptions() {
        return new CommandOptionType[0];
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Bot.playerManager.getGuildMusicManager(event.getGuild().getIdLong()).skip();
        event.reply(Emoji.SUCCESS.getCode() + " Skipped!").queue();
    }
}
