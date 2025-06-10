package io.lolyay.commands.music;

import io.lolyay.infusiadc.Bot;
import io.lolyay.infusiadc.MusicBot.commands.MusicCommandType;
import io.lolyay.infusiadc.Utils.Emoji;
import net.dv8tion.jda.api.entities.Message;

public class ForceskipCmd implements MusicCommandType
{

    @Override
    public String getName() {
        return "m skip";
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void execute(Message message, String[] args) {
        Bot.playerManager.getGuildMusicManager(message.getGuild().getIdLong()).skip();
        message.reply(Emoji.SUCCESS.getCode() + " Skipped!").queue();

    }
}
