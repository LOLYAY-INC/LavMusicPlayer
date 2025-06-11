package io.lolyay.commands.manager;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public interface CommandOptionType {
    public String getName();

    public String getDescription();

    public OptionType getType();


}
