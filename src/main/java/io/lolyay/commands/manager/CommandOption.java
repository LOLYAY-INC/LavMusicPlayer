package io.lolyay.commands.manager;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandOption implements CommandOptionType {
    private String name;
    private String description;
    private OptionType type;
    private boolean required;
    private boolean greedy;

    public CommandOption(String name, String description, OptionType type, boolean required, boolean greedy) {
        this.greedy = greedy;
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

    public boolean isGreedy() { // can Contain Whitespace
        return greedy;
    }

    public void setGreedy(boolean greedy) {
        this.greedy = greedy;
    }
}
