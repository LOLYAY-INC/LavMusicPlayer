
package io.lolyay.commands.info;


import io.lolyay.JdaMain;
import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.embedmakers.StatusEmbedGenerator;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.utils.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class StatusCommand implements Command {


    @Override
    public String getName() {
        return "status";
    }

    @Override
    public String getDescription() {
        return "Gets the current Status";
    }

    @Override
    public CommandOption[] getOptions() {
        return new CommandOption[0];
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = JdaMain.playerManager.getGuildMusicManager(event.getGuild().getIdLong());
        event.replyEmbeds(StatusEmbedGenerator.generate(musicManager).build()).queue();
    }
}
