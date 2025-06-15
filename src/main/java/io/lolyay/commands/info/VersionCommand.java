
package io.lolyay.commands.info;

import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.config.ConfigManager;
import io.lolyay.utils.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public class VersionCommand implements Command
{


    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getDescription() {
        return "Get the version of the bot";
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
        event.reply(Emoji.SEARCH.getCode() + " The bot is running: " + ConfigManager.getConfig("version") + " of LavMusicBot \n " +
                "**Github**: <https://github.com/LOLYAY-INC/LavMusicBot>\n " +
                "**Get status of this server with /status!**").queue();

    }
}
