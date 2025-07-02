package io.lolyay.commands.manager;

import io.lolyay.commands.slash.info.StatusCommand;
import io.lolyay.commands.slash.info.VersionCommand;
import io.lolyay.commands.slash.music.*;

import java.util.Arrays;
import java.util.List;

public class CommandRegistry {
    public static List<Command> getCommands() {
        return Arrays.asList(
                new GetLyricsCommand(),
                new PauseCommand(),
                new PlayCommand(),
                new RepeatModeCommand(),
                new ResumeCommand(),
                new ResumeQueueCommand(),
                new ShuffleCommand(),
                new SkipCommand(),
                new StopCommand(),
                new StopLiveLyricsCommand(),
                new VolumeCommand(),
                new StatusCommand(),
                new VersionCommand()
        );
    }
}
