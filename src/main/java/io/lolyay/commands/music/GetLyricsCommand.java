package io.lolyay.commands.music;


import io.lolyay.JdaMain;
import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.embedmakers.LyricsEmbedGenerator;
import io.lolyay.lyrics.CustomLyricsGetter;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.utils.Emoji;
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


        event.deferReply().queue();
        CustomLyricsGetter.getLyrics(musicManager.getQueue().getFirst().track().getInfo().getTitle()).thenAcceptAsync(
                lyrics -> {
                    if (lyrics == null) {
                        event.getHook().sendMessage(Emoji.ERROR.getCode() + " No Lyrics found for this song").queue();
                        return;
                    }
                    event.getHook().sendMessageEmbeds(LyricsEmbedGenerator.generate(lyrics).build()).queue();
                }
        );

    }


}