package io.lolyay.commands.manager;

public abstract class Command {
    public abstract String getName();

    public abstract String getDescription();

    public abstract CommandOptionType[] getOptions();

    public boolean requiresPermission() {
        return true;
    }

    public abstract void execute(CommandContext context);

}
