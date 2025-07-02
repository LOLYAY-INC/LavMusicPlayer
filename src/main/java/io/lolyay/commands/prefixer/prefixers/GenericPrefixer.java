package io.lolyay.commands.prefixer.prefixers;

import io.lolyay.commands.manager.Command;
import io.lolyay.commands.prefixer.AbstractPrefixer;

public class GenericPrefixer extends AbstractPrefixer {

    private final Command command;

    public GenericPrefixer(Command command) {
        this.command = command;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public Command getReferer() {
        return command;
    }
}
