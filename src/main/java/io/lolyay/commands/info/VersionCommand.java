
package io.lolyay.commands.info;

import io.lolyay.JdaMain;
import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.config.ConfigLoader;
import io.lolyay.config.ConfigManager;
import io.lolyay.utils.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;


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
        event.reply(Emoji.SEARCH + "The bot is running version " + ConfigManager.getConfig("version") + " of LavMusicBot, \n **Github**: https://github.com/LOLYAY-INC/LavMusicBot").queue();

    }
}
