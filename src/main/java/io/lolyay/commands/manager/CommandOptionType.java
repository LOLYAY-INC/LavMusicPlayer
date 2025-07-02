package io.lolyay.commands.manager;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public interface CommandOptionType {
    String getName();

    String getDescription();

    OptionType getType();

    boolean isRequired();

    boolean isGreedy();


}
