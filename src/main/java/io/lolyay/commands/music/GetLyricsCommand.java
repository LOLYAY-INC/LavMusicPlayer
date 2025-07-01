package io.lolyay.commands.music;


import io.lolyay.JdaMain;
import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.config.ConfigManager;
import io.lolyay.embedmakers.LyricsEmbedGenerator;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.lyrics.getters.MusixMatchGetter;
import io.lolyay.musicbot.lyrics.live.SyncedLyricsPlayer;
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
        if (!ConfigManager.getConfigBool("lyrics-enabled")) {
            event.reply(Emoji.ERROR.getCode() + " Lyrics are currently disabled").queue();
            return;
        }

        if (musicManager.getQueue().isEmpty()) {
            event.reply(Emoji.ERROR.getCode() + " Queue is empty").queue();
            return;
        }



        event.deferReply().queue();
        new MusixMatchGetter().getLyrics(musicManager.getQueue().getFirst().track().getInfo().getTitle()).thenAcceptAsync(
                lyrics -> {
                    if (lyrics == null) {
                        event.getHook().sendMessage(Emoji.ERROR.getCode() + " No Lyrics found for this song").queue();
                        return;
                    }

                    event.getHook().sendMessageEmbeds(LyricsEmbedGenerator.generate(lyrics).build()).queue(message -> {
                        if (ConfigManager.getConfigBool("live-lyrics-enabled")) {
                            try {
                                SyncedLyricsPlayer.start(event.getGuild().getIdLong(), message);
                                SyncedLyricsPlayer.nextSong(message.getGuildIdLong(), musicManager.getQueue().getFirst().track().getInfo().getTitle(), musicManager.getQueue().getFirst().startTime().getTime());
                            } catch (Exception e) {
                                Logger.err("Error starting synced lyrics: " + e.getMessage());
                                event.getHook().sendMessage(Emoji.ERROR.getCode() + " Error starting synced lyrics: " + e.getMessage()).queue();
                            }
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