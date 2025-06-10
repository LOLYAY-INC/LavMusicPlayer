package io.lolyay.commands.music;

import io.lolyay.infusiadc.Bot;
import io.lolyay.infusiadc.MusicBot.Rewrite.GuildMusicManager;
import io.lolyay.infusiadc.MusicBot.commands.MusicCommandType;
import io.lolyay.infusiadc.Utils.Emoji;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCmd implements MusicCommandType {

    @Override
    public String getName() {
        return "m play";
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }

    @Override
    public String[] getAliases() {
        // You can add aliases like "p" here if you want
        return new String[]{"p"};
    }

    @Override
    public void execute(Message message, String[] args) {
        final Member member = message.getMember();
        final Guild guild = message.getGuild();
        final GuildVoiceState selfVoiceState = guild.getSelfMember().getVoiceState();

        // 1. --- Pre-flight Checks ---

        // Check if the user provided a query or an attachment
        if (args.length == 0 && message.getAttachments().isEmpty()) {
            message.reply(Emoji.ERROR.getCode() + " You must provide a search query or a file to play!").queue();
            return;
        }

        // Check if the user is in a voice channel
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (memberVoiceState == null || !memberVoiceState.inAudioChannel()) {
            message.reply(Emoji.ERROR.getCode() + " You must be in a voice channel to use this command!").queue();
            return;
        }

        final VoiceChannel memberChannel = memberVoiceState.getChannel().asVoiceChannel();

        // Check if the bot has permissions to connect and speak
        if (!guild.getSelfMember().hasPermission(memberChannel, Permission.VOICE_CONNECT)) {
            message.reply(Emoji.ERROR.getCode() + " I don't have permission to connect to your voice channel!").queue();
            return;
        }
        if (!guild.getSelfMember().hasPermission(memberChannel, Permission.VOICE_SPEAK)) {
            message.reply(Emoji.ERROR.getCode() + " I don't have permission to speak in your voice channel!").queue();
            return;
        }

        // 2. --- Argument Parsing and Voice Connection ---

        String query = parseQuery(message, args);
        final AudioManager audioManager = guild.getAudioManager();

        // Connect if not already in the user's channel
        if (!selfVoiceState.inAudioChannel() || selfVoiceState.getChannel() != memberChannel) {
            audioManager.openAudioConnection(memberChannel);
            // JDA handles the connection asynchronously, we can proceed.
        }

        // 3. --- Search and Queue ---

        // Let the user know we're working on it
        message.reply(Emoji.LOADING.getCode() + " Searching for `" + query + "`...").queue(loadingMessage -> {

            GuildMusicManager musicManager = Bot.playerManager.getGuildMusicManager(guild.getIdLong());

            Bot.playerManager.searchTrack(query, member,
                    // --- SUCCESS CALLBACK ---
                    (track) -> {
                        final boolean isPlayingNow = musicManager.getQueManager().isEmpty();
                        musicManager.queueTrack(track);

                        String response;
                        if (isPlayingNow) {
                            response = Emoji.PLAY.getCode() + " Now Playing: **" + track.getTrack().getInfo().getTitle() + "**";
                        } else {
                            int position = musicManager.getQueManager().getQueue().size();
                            response = Emoji.SUCCESS.getCode() + " Added to queue: **" + track.getTrack().getInfo().getTitle() + "**"
                                    + "\n" + Emoji.MUSIC.getCode() + " Position: **#" + position + "**";
                        }
                        loadingMessage.editMessage(response).queue();
                    },
                    // --- FAILURE CALLBACK ---
                    () -> {
                        String response = Emoji.ERROR.getCode() + " Could not find any results for `" + query + "`.";
                        loadingMessage.editMessage(response).queue();
                    }
            );
        });
    }

    /**
     * Parses the command arguments and message attachments to get a playable query.
     */
    private String parseQuery(Message message, String[] args) {
        if (args.length > 0) {
            String fullArgs = String.join(" ", args);
            // Removes the angle brackets Discord sometimes adds to links
            if (fullArgs.startsWith("<") && fullArgs.endsWith(">")) {
                return fullArgs.substring(1, fullArgs.length() - 1);
            }
            return fullArgs;
        }
        // If no text arguments, use the URL of the first attachment
        return message.getAttachments().get(0).getUrl();
    }
}