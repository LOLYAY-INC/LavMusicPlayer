
package io.lolyay.commands.music;


import io.lolyay.JdaMain;
import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PauseCommand implements Command {


    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getDescription() {
        return "Pauses the current Track, resume with /resume";
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
        GuildMusicManager musicManager = JdaMain.playerManager.getGuildMusicManager(event.getGuild().getIdLong());
        if(!musicManager.isPlaying()){
            event.reply(Emoji.ERROR.getCode() + " No Track is playing, couldn't pause!").queue();
            return;
        }
        if(musicManager.isPaused()){
            event.reply(Emoji.ERROR.getCode() + " Already paused!").queue();
            return;
        }
        musicManager.pause();
        event.reply(Emoji.SUCCESS.getCode() + " Paused Playback!").queue();
    }
}
