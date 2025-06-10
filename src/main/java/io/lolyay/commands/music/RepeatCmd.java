
package io.lolyay.commands.music;

import io.lolyay.infusiadc.Bot;
import io.lolyay.infusiadc.MusicBot.audio.que.RepeatMode;
import io.lolyay.infusiadc.MusicBot.commands.MusicCommandType;
import io.lolyay.infusiadc.Settingsmanager.FlagManager.FlagManager;
import io.lolyay.infusiadc.Utils.Emoji;
import net.dv8tion.jda.api.entities.Message;


public class RepeatCmd implements MusicCommandType
{

    @Override
    public String getName() {
        return "m repeat";
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void execute(Message message, String[] args) {
        String argsstr = String.join(" ", args);
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
            message.reply(Emoji.ERROR.getCode() + " Invalid repeat mode: `" + argsstr + "` Valid modes: `false`, `true`, `all`, `one`, `single`").queue();
            return;
        }
        FlagManager.setFlag("MUSICBOT_REPEATMODE", value.name());
        Bot.playerManager.getGuildMusicManager(message.getGuild().getIdLong()).setRepeatMode(value);
        message.reply(Emoji.SUCCESS.getCode() + "Repeat mode is now `"+value.getUserFriendlyName()+"`").queue();
    }
}
