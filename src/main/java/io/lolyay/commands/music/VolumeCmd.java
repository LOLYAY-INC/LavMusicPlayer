
package io.lolyay.commands.music;

import io.lolyay.infusiadc.MusicBot.commands.MusicCommandType;
import io.lolyay.infusiadc.Settingsmanager.FlagManager.FlagManager;
import io.lolyay.infusiadc.Utils.Emoji;
import net.dv8tion.jda.api.entities.Message;


public class VolumeCmd implements MusicCommandType
{


    @Override
    public String getName() {
        return "m volume";
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
       // Settings settings = event.getClient().getSettingsFor(message.getGuild());
            int nvolume;
            try{
                nvolume = Integer.parseInt(args[0]);
            }catch(NumberFormatException e){
                nvolume = -1;
            }
            if(nvolume<0 || nvolume>150)
                message.reply(Emoji.ERROR.getCode() +" Volume must be a valid integer between 0 and 150!");
            else
            {
                //settings.setVolume(nvolume);
                message.reply( Emoji.SUCCESS.getCode() + nvolume +" Volume changed from `"+ FlagManager.getFlag("MUSICBOT_VOLUME").valuetoString() +"` to `"+nvolume+"`").queue();
                FlagManager.setFlag("MUSICBOT_VOLUME", nvolume);
            }


    }
}
