package io.lolyay.commands.music;


import io.lolyay.JdaMain;
import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.embedmakers.StatusEmbedGenerator;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.utils.Emoji;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements Command {

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        // You might want to update this if it's a fully functional command now
        return "Plays a song from a given search!";
    }

    @Override
    public CommandOption[] getOptions() {
        return new CommandOption[]{new CommandOption("song", "The song's name or URL to play", OptionType.STRING, true)};
    }

    @Override
    public boolean requiresPermission() {
        return true; // As per your original code
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        final Member member = event.getMember();
        final Guild guild = event.getGuild();
        final GuildVoiceState selfVoiceState = guild.getSelfMember().getVoiceState();

        // --- 1. Pre-flight Checks ---

        // This should not happen in a guild command, but it's a good safety check
        if (member == null) return;

        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (memberVoiceState == null || !memberVoiceState.inAudioChannel()) {
            event.reply(Emoji.ERROR.getCode() + " You must be in a voice channel to use this command!").setEphemeral(true).queue();
            return;
        }

        final VoiceChannel memberChannel = memberVoiceState.getChannel().asVoiceChannel();

        // Check bot permissions
        if (!guild.getSelfMember().hasPermission(memberChannel, Permission.VOICE_CONNECT)) {
            event.reply(Emoji.ERROR.getCode() + " I don't have permission to connect to your voice channel!").setEphemeral(true).queue();
            return;
        }
        if (!guild.getSelfMember().hasPermission(memberChannel, Permission.VOICE_SPEAK)) {
            event.reply(Emoji.ERROR.getCode() + " I don't have permission to speak in your voice channel!").setEphemeral(true).queue();
            return;
        }

        // --- 2. Defer Reply and Connect ---

        // Defer the reply immediately. This shows "Bot is thinking..."
        // and gives us time to search for the track.
        event.deferReply().queue();

        // Connect if not already in the user's channel, or switch to it
        final AudioManager audioManager = guild.getAudioManager();
        if (!selfVoiceState.inAudioChannel() || selfVoiceState.getChannel() != memberChannel) {
            audioManager.openAudioConnection(memberChannel);
        }

        // --- 3. Search and Queue ---

        final String query = event.getOption("song").getAsString();
        final GuildMusicManager musicManager = JdaMain.playerManager.getGuildMusicManager(guild.getIdLong());

        JdaMain.playerManager.searchTrack(query, member,
                // --- SUCCESS CALLBACK ---
                (track) -> {
                    final boolean isPlayingNow = musicManager.getQueManager().isEmpty();
                    musicManager.queueTrack(track);

                    String response;
                    if (isPlayingNow) {
                        response = Emoji.PLAY.getCode() + " Now Playing: **" + track.track().getInfo().getTitle() + "**";
                    } else {
                        int position = musicManager.getQueManager().getQueue().size();
                        response = Emoji.SUCCESS.getCode() + " Added to queue: **" + track.track().getInfo().getTitle() + "**"
                                + "\n" + Emoji.MUSIC.getCode() + " Position: **#" + position + "**";
                    }
                    // Use the hook to send the final response
                    event.getHook().sendMessage(response).queue();
                    event.getHook().sendMessageEmbeds(StatusEmbedGenerator.generate(musicManager).build()).queue();
                },
                // --- FAILURE CALLBACK ---
                () -> {
                    String response = Emoji.ERROR.getCode() + " Could not find any results for `" + query + "`.";
                    event.getHook().sendMessage(response).queue();
                }
        );
    }
}