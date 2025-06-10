package io.lolyay.commands.Manager;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandOption implements CommandOptionType {
    private String name;
    private String description;
    private OptionType type;
    private boolean required;

    public CommandOption(String name, String description, OptionType type, boolean required) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
    }
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OptionType getType() {
        return type;
    }

    public void setType(OptionType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
