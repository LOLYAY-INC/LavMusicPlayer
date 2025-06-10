
package io.lolyay.commands.music;

import io.lolyay.JdaMain;
import io.lolyay.commands.Manager.Command;
import io.lolyay.commands.Manager.CommandOptionMultiple;
import io.lolyay.musicbot.queue.RepeatMode;
import io.lolyay.utils.Emoji;
import io.lolyay.utils.KVPair;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import static io.lolyay.commands.Manager.CommandOptionMultiple.enumToKVPairArray;


public class RepeatModeCommand implements Command {


    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getDescription() {
        return "Sets the Repeatmode";
    }

    @Override
    public CommandOptionMultiple[] getOptions() {
        KVPair<String, String>[] repeatOptions = enumToKVPairArray(RepeatMode.class);
        return new CommandOptionMultiple[] {
                new CommandOptionMultiple("mode", "The Repeatmode", repeatOptions, OptionType.STRING)
        };
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String argsstr = event.getOption("mode").getAsString();
        RepeatMode value;
        //  Settings settings = event.getClient().getSettingsFor(event.getGuild());
        if(argsstr.equalsIgnoreCase("false") || argsstr.equalsIgnoreCase("off"))
        {
            value = RepeatMode.OFF;
        }
        else if(argsstr.equalsIgnoreCase("true") || argsstr.equalsIgnoreCase("on") || argsstr.equalsIgnoreCase("all"))
        {
            value = RepeatMode.ALL;
        }
        else if(argsstr.equalsIgnoreCase("one") || argsstr.equalsIgnoreCase("single"))
        {
            value = RepeatMode.SINGLE;
        }
        else
        {
            event.reply(Emoji.ERROR.getCode() + " Invalid repeat mode: `" + argsstr + "` Valid modes: `false`, `true`, `all`, `one`, `single`").queue();
            return;
        }
        JdaMain.playerManager.getGuildMusicManager(event.getGuild().getIdLong()).setRepeatMode(value);
        event.reply(Emoji.SUCCESS.getCode() + " Repeat mode is now `"+value.getUserFriendlyName()+"`").queue();
    }
}
