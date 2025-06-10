
package io.lolyay.commands.music;


import io.lolyay.JdaMain;
import io.lolyay.commands.Manager.Command;
import io.lolyay.commands.Manager.CommandOption;
import io.lolyay.commands.Manager.CommandOptionType;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class StopCommand implements Command {


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
    public void execute(SlashCommandInteractionEvent event) {
        if(JdaMain.playerManager.getGuildMusicManager(event.getGuild().getIdLong()).isPlaying()){
            JdaMain.playerManager.getGuildMusicManager(event.getGuild().getIdLong()).stop();
            event.reply(Emoji.SUCCESS.getCode() + " Stopped Playback and Cleared the queue!").queue();
        }
        else
            event.reply(Emoji.ERROR.getCode() + " No Track is playing, couldn't stop!").queue();

    }
}
