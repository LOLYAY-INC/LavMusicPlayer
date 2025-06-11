
package io.lolyay.commands.music;

import io.lolyay.JdaMain;
import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.utils.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;


public class VolumeCommand implements Command
{


    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String getDescription() {
        return "Sets the volume for the current Track";
    }

    @Override
    public CommandOption[] getOptions() {
        return new CommandOption[]{
                new CommandOption("volume", "The volume to set to", OptionType.INTEGER, true)
        };
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int nvolume = event.getOption("volume").getAsInt();
        if(nvolume<0 || nvolume>150)
            event.reply(Emoji.ERROR.getCode() +" Volume must be a valid integer between 0 and 150!").queue();
        else
        {
            event.reply( Emoji.SUCCESS.getCode() + " Volume changed from `"+ (int) JdaMain.playerManager.getGuildMusicManager(event.getGuild().getIdLong()).getVolume() +"` to `"+nvolume+"`").queue();
            JdaMain.playerManager.getGuildMusicManager(event.getGuild().getIdLong()).setVolume(nvolume);
        }

    }
}
