package io.lolyay.utils;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;

public class SendDM {
    public static void sendDM(User user, String message) {
       PrivateChannel channel =  user.openPrivateChannel().complete();
       channel.sendMessage(message).complete();

    }
}
