package io.lolyay.commands.Manager;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public interface CommandOptionType {
    public String getName();

    public String getDescription();

    public OptionType getType();


}
