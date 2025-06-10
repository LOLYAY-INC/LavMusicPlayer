
package io.lolyay.commands.music;


import io.lolyay.JdaMain;
import io.lolyay.commands.Manager.Command;
import io.lolyay.commands.Manager.CommandOptionType;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SkipCommand implements Command {


    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skips the current track and plays the next one in the queue.";
    }

    @Override
    public CommandOptionType[] getOptions() {
        return new CommandOptionType[0];
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MusicAudioTrack currentTrack = JdaMain.playerManager.getGuildMusicManager(event.getGuild().getIdLong()).skip();
        if(currentTrack == null)
            event.reply(Emoji.SUCCESS.getCode() + " No more Tracks to play, stopping!").queue();
        else
            event.reply(Emoji.SUCCESS.getCode() + " Skipped: `" + currentTrack.track().getInfo()
                    .getTitle() + "´ (Requested by " + currentTrack.userData().userName() + " )").queue();

    }
}
