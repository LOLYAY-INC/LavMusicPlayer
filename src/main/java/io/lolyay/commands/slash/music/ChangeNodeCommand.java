
package io.lolyay.commands.slash.music;


import dev.arbjerg.lavalink.client.LavalinkNode;
import io.lolyay.JdaMain;
import io.lolyay.commands.manager.Command;
import io.lolyay.commands.manager.CommandContext;
import io.lolyay.commands.manager.CommandOption;
import io.lolyay.utils.Emoji;
import io.lolyay.utils.Logger;

import java.time.Duration;
import java.util.Random;

public class ChangeNodeCommand extends Command {


    @Override
    public String getName() {
        return "changenode";
    }

    @Override
    public String getDescription() {
        return "Changes the Lavalink Node to another random one";
    }

    @Override
    public CommandOption[] getOptions() {
        return new CommandOption[0];
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }

    @Override
    public void execute(CommandContext event) {
        LavalinkNode nextNode = JdaMain.lavalinkClient.getNodes().get(new Random().nextInt(JdaMain.lavalinkClient.getNodes().size()));
        JdaMain.lavalinkClient.getOrCreateLink(event.getGuild().getIdLong()).transferNode$lavalink_client(
                nextNode,
                Duration.ofSeconds(3)
        );
        Logger.log("Changed Node to " + nextNode.getBaseUri() + " for " + event.getGuild().getName() + " (" + event.getGuild().getIdLong() + ")");
        event.reply(Emoji.SUCCESS.getCode() + " Changed Node to " + nextNode.getName()).queue();
    }
}
