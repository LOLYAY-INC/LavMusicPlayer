package io.lolyay.events;

import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnGuildVoiceUpdate extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        if(event.getChannelLeft() == null) return;
        if(event.getGuild().getSelfMember().getVoiceState() == null) return;
        if(event.getGuild().getSelfMember().getVoiceState().getChannel() == null) return;
        if(event.getChannelLeft().asVoiceChannel().getMembers().size() > 1) return;
        if(event.getChannelLeft().asVoiceChannel().getIdLong() == event.getGuild().getSelfMember().getVoiceState().getChannel().getIdLong()) {
            Logger.log("Alone in voice channel, leaving...");
            event.getJDA().getDirectAudioController().disconnect(event.getGuild());
        }
    }
}
