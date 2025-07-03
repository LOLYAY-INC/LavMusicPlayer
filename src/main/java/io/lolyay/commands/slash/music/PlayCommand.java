package io.lolyay.commands.slash.music;

import io.lolyay.JdaMain;
import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandContext;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.lyrics.live.SyncedLyricsPlayer;
import io.lolyay.musicbot.search.PlaylistData;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Emoji;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlayCommand extends Command {

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Plays a song from a given search!";
    }

    @Override
    public CommandOption[] getOptions() {
        return new CommandOption[]{new CommandOption("song", "The song's name or URL to play", OptionType.STRING, true, true)};
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }

    @NotNull
    private static String getResponse(MusicAudioTrack track, boolean isPlayingNow, GuildMusicManager musicManager) {
        String response;
        if (isPlayingNow) {
            response = Emoji.PLAY.getCode() + " Now Playing: **" + track.trackInfo().title() + "**";
        } else {
            int position = musicManager.getQueManager().getQueue().size();
            response = Emoji.SUCCESS.getCode() + " Added to queue: **" + track.trackInfo().title() + "**"
                    + "\n" + Emoji.MUSIC.getCode() + " Position: **#" + position + "**";
        }
        return response;
    }

    @Override
    public void execute(CommandContext context) {
        final Member member = context.getMember();
        final Guild guild = context.getGuild();

        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (memberVoiceState == null || !memberVoiceState.inAudioChannel()) {
            context.reply(Emoji.ERROR.getCode() + " You must be in a voice channel to use this command!").setEphemeral(true).queue();
            return;
        }

        final VoiceChannel memberChannel = memberVoiceState.getChannel().asVoiceChannel();

        // Check bot permissions
        if (!guild.getSelfMember().hasPermission(memberChannel, Permission.VOICE_CONNECT)) {
            context.reply(Emoji.ERROR.getCode() + " I don't have permission to connect to your voice channel!").setEphemeral(true).queue();
            return;
        }
        if (!guild.getSelfMember().hasPermission(memberChannel, Permission.VOICE_SPEAK)) {
            context.reply(Emoji.ERROR.getCode() + " I don't have permission to speak in your voice channel!").setEphemeral(true).queue();
            return;
        }

        context.deferReply(false);

        final String query = context.getOption("song").getAsString();
        final GuildMusicManager musicManager = JdaMain.playerManager.getGuildMusicManager(guild.getIdLong());

        JdaMain.playerManager.searchWithDefaultOrder(query, Optional.of(member), (search) -> {
            switch (search.result().getStatus()) {
                case SUCCESS -> {
                    MusicAudioTrack track = search.track().get();
                    JdaMain.playerManager.connect(memberChannel);
                    SyncedLyricsPlayer.precacheSong(track.trackInfo().title());
                    final boolean isPlayingNow = musicManager.getQueManager().isEmpty();
                    musicManager.queueTrack(track);

                    String response = getResponse(track, isPlayingNow, musicManager);
                    context.reply(response).queue();
                }
                case PLAYLIST -> {
                    PlaylistData playlistData = search.playlistData();
                    JdaMain.playerManager.connect(memberChannel);

                    MusicAudioTrack track = playlistData.selectedTrack();
                    SyncedLyricsPlayer.precacheSong(track.trackInfo().title());

                    final boolean isPlayingNow = musicManager.getQueManager().isEmpty();

                    musicManager.queueTrack(track);

                    String response = getResponse(track, isPlayingNow, musicManager);
                    context.reply(response).queue();

                }
                case NOT_FOUND -> {
                    String response = Emoji.ERROR.getCode() + " Could not find any results for `" + query + "`.";
                    context.reply(response).queue();
                }
                case ERROR -> {
                    String response = Emoji.ERROR.getCode() + " An error occurred: `" + search.result().getMessage() + "`";
                    context.reply(response).queue();
                }
            }
        }, guild.getIdLong());
    }
}