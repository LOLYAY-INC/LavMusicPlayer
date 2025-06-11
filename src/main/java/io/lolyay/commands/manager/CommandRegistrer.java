package io.lolyay.commands.manager;

import io.lolyay.JdaMain;
import io.lolyay.commands.info.VersionCommand;
import io.lolyay.commands.music.*;
import io.lolyay.utils.KVPair;
import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.*;

public class CommandRegistrer {
    private static final ArrayList<SlashCommandData> commands = new ArrayList<>();
    private static final Map<String, Command> commandstorun = new HashMap<>();

    private static final Command[] commandstoberegistered = {
            new RepeatModeCommand(),
            new SkipCommand(),
            new VolumeCommand(),
            new PlayCommand(),
            new StopCommand(),
            new VersionCommand(),

    };

    public static void runCommand(String name, SlashCommandInteractionEvent event) {
        Command command = commandstorun.get(name);
        if (command.requiresPermission() && !canRunCommand(Objects.requireNonNull(event.getMember()))) {
            event.reply("You don't have permission to use this command").setEphemeral(true).queue();
            return;
        }
        command.execute(event);

    }

    public static void register(Guild guild) {
        commands.clear();
        for (Command command : commandstoberegistered) {
            registerCommandImpl(command);
        }
        clearGuildCommands(guild.retrieveCommands().complete(), guild);
        guild.updateCommands().addCommands(commands).complete();
        Logger.debug("Registered Commands for guild: " + guild.getName());
    }

    public static void register() { // Bot command
        commands.clear();
        for (Command command : commandstoberegistered) {
            registerCommandImpl(command);
        }

        clearBotCommands(JdaMain.jda.retrieveCommands().complete());
        JdaMain.jda.updateCommands().addCommands(commands).complete();
        // not needed : registertorun();
        Logger.debug("Registered Commands");
        Logger.success("Bot is now Ready, You can use it now!");

    }

    private static void clearGuildCommands(List<net.dv8tion.jda.api.interactions.commands.Command> commandList, Guild guild) {
        for (net.dv8tion.jda.api.interactions.commands.Command command : commandList) {
            guild.deleteCommandById(command.getId()).complete();
            Logger.debug("Deleted Guild Command: " + command.getName());
        }
    }

    private static void clearBotCommands(List<net.dv8tion.jda.api.interactions.commands.Command> commandList) {
        for (net.dv8tion.jda.api.interactions.commands.Command command : commandList) {
            try {
                JdaMain.jda.deleteCommandById(command.getId()).complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Logger.debug("Deleted Bot Command: " + command.getName());
        }
    }

    public static void registerCommandsToRun() {
        for (Command command : commandstoberegistered) {
            registerCommandToRunImpl(command);
        }
    }

    private static void registerCommandImpl(Command command) {
        if (command.getOptions() == null) {
            SlashCommandData slash = Commands.slash(command.getName(),command.getDescription());
            commands.add(slash);
        }
        if (command.getOptions() instanceof CommandOption[]) {
            commands.add(commandOptionBuilderImpl(command));
        }
        else if (command.getOptions() instanceof CommandOptionMultiple[]) {
            commands.add(commandOptionMultipleBuilderImpl(command));
        } else { // Left unchanged! Registering, but warning
            Logger.warn("Command %s has no options, use with ´CommandOption[0]´ if no arguments are needed!".formatted(command.getName()));
            SlashCommandData slash = Commands.slash(command.getName(), command.getDescription());
            commands.add(slash);
        }
        registerCommandToRunImpl(command);
    }

    private static void registerCommandToRunImpl(Command command) {
        commandstorun.put(command.getName(), command);
    }

    private static SlashCommandData commandOptionBuilderImpl(Command command){
        String name = command.getName();
        String description = command.getDescription();
        CommandOption[] options = (CommandOption[]) command.getOptions();


        SlashCommandData slash = Commands.slash(name,description);
        for (CommandOption option : options) {
            slash.addOption(option.getType(), option.getName(), option.getDescription(),option.isRequired());
        }
        return slash;
    }


    private static SlashCommandData commandOptionMultipleBuilderImpl(Command command) {
        String name = command.getName();
        String description = command.getDescription();
        CommandOptionMultiple[] options = (CommandOptionMultiple[]) command.getOptions();


        SlashCommandData slash = Commands.slash(name, description);
        for (CommandOptionMultiple option : options) {
            OptionData data = new OptionData(option.getType(), option.getName(), option.getDescription(),option.isRequired());
            for (KVPair<String, String> pair : option.getOptions()) {
                data.addChoice(pair.getKey(), pair.getValue());
            }
            slash.addOptions(data);
        }
        return slash;
    }

    private static boolean canRunCommand(Member member) {
     /* WIP  for (Role role : member.getRoles()) {
            if () {
                return true;
            }
        }*/
        return true;
    }
}
