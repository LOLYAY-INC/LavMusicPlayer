
package io.lolyay.commands.slash.music;


import io.lolyay.JdaMain;
import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandContext;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.utils.Emoji;

public class StopCommand extends Command {


    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Stops the current Playback and Clears the queue.";
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
    public void execute(CommandContext event) {
        if(JdaMain.playerManager.getGuildMusicManager(event.getGuild().getIdLong()).isPlaying()){
            JdaMain.playerManager.getGuildMusicManager(event.getGuild().getIdLong()).stop();
            event.reply(Emoji.SUCCESS.getCode() + " Stopped Playback and Cleared the queue!").queue();
        }
        else
            event.reply(Emoji.ERROR.getCode() + " No Track is playing, couldn't stop!").queue();

    }
}
