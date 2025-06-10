
package io.lolyay.commands;

import io.lolyay.infusiadc.ECommands.MusicBotAdminMode;
import io.lolyay.infusiadc.Utils.Emoji;
import net.dv8tion.jda.api.entities.Message;

public class ToggleAdminModeComand implements MusicCommandType {

    @Override
    public String getName() {
        return "m ta";
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void execute(Message message, String[] args) {
        MusicBotAdminMode.toggleAdminMode();
        message.reply(Emoji.SUCCESS.getCode() + " Admin mode is " + (MusicBotAdminMode.isAdminMode() ? "enabled" : "disabled")).queue();


    }
}
