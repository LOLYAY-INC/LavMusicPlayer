
package io.lolyay.commands.music;

import io.lolyay.infusiadc.Bot;
import io.lolyay.infusiadc.Commands.Manager.CommandOptionMultiple;
import io.lolyay.infusiadc.Commands.Manager.KVPair;
import io.lolyay.infusiadc.MusicBot.audio.que.RepeatMode;
import io.lolyay.infusiadc.MusicBot.commands.SlashMusicCommandType;
import io.lolyay.infusiadc.Settingsmanager.FlagManager.FlagManager;
import io.lolyay.infusiadc.Utils.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import static io.lolyay.infusiadc.Commands.Manager.CommandOptionMultiple.enumToKVPairArray;


public class SlashRepeatCmd implements SlashMusicCommandType {


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
        Bot.playerManager.getGuildMusicManager(event.getGuild().getIdLong()).setRepeatMode(value);
        FlagManager.setFlag("MUSICBOT_REPEATMODE", value.name());
        event.reply(Emoji.SUCCESS.getCode() + "Repeat mode is now `"+value.getUserFriendlyName()+"`").queue();
    }
}
