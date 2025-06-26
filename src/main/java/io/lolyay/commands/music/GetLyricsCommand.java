package io.lolyay.commands.music;


import io.lolyay.JdaMain;
import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.config.ConfigManager;
import io.lolyay.embedmakers.LyricsEmbedGenerator;
import io.lolyay.lyrics.getters.MusixMatchGetter;
import io.lolyay.lyrics.live.SyncedLyricsPlayer;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.utils.Emoji;
import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class GetLyricsCommand implements Command {

    @Override
    public String getName() {
        return "lyrics";
    }

    @Override
    public String getDescription() {
        return "Gets the lyrics of the currently playing song";
    }

    @Override
    public CommandOption[] getOptions() {
        return null;
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = JdaMain.playerManager.getGuildMusicManager(event.getGuild().getIdLong());

        // DOESNT WORK (FIXED?)
        //    event.reply(Emoji.ERROR.getCode() + " This Command is currently unavailable").queue();

        if (!ConfigManager.getConfigBool("lyrics-enabled")) {
            event.reply(Emoji.ERROR.getCode() + " Lyrics are currently disabled").queue();
            return;
        }

        if (musicManager.getQueue().isEmpty()) {
            event.reply(Emoji.ERROR.getCode() + " Queue is empty").queue();
            return;
        }



        event.deferReply().queue();
        new MusixMatchGetter().getLyrics(musicManager.getQueue().getFirst().track().getInfo().getTitle(), musicManager.getQueue().getFirst().startTime()).thenAcceptAsync(
                lyrics -> {
                    if (lyrics == null) {
                        event.getHook().sendMessage(Emoji.ERROR.getCode() + " No Lyrics found for this song").queue();
                        return;
                    }
                    SyncedLyricsPlayer player = SyncedLyricsPlayer.fromJson(lyrics.liveSection());
                    event.getHook().sendMessageEmbeds(LyricsEmbedGenerator.generate(lyrics).build()).queue(message -> {
                        if (ConfigManager.getConfigBool("live-lyrics-enabled")) {
                            player.startPlayback(message, musicManager.getQueue().getFirst().startTime().getTime() - Integer.parseInt(ConfigManager.getConfig("live-lyrics-ping-compensation")));
                        }
                    });
                }
        ).exceptionally((e) -> {
            Logger.err("Error getting lyrics: " + e.getMessage());
            event.getHook().sendMessage(Emoji.ERROR.getCode() + " Error getting lyrics: " + e.getMessage()).queue();
            e.printStackTrace();
            return null;
        });

    }


}