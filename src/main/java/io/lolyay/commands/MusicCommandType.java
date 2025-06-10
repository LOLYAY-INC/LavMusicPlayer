package io.lolyay.commands;

import io.lolyay.infusiadc.ECommands.Manager.ECommand;
import net.dv8tion.jda.api.entities.Message;

public interface MusicCommandType extends ECommand {
    public String getName();
    public boolean requiresPermission();
    public String[] getAliases();

    public void execute(Message message, String[] args);


}
