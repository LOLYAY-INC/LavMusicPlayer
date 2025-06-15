package io.lolyay.embedmakers;

import io.lolyay.musicbot.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class StatusEmbedGenerator {
    public static EmbedBuilder generate(GuildMusicManager musicManager) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(genTitle(musicManager));
        builder.addField("","**" + getTitle(musicManager) + "** by **" + getArtist(musicManager) + "**",false);
        builder.addField("**Queue:**",musicManager.getQueManager().getQueue().size() + " tracks left",false);
        builder.addField("**Repeat:**",musicManager.getRepeatMode().getEmoji() + " - " + musicManager.getRepeatMode().getUserFriendlyName(),false);
        builder.addField("**Volume:**",musicManager.getVolume() + " / 100",false);
        builder.setThumbnail(getImageURL(musicManager));
        builder.setColor(genColor(musicManager.isPlaying(),musicManager.isPaused()));
        return builder;
    }


    private static String genPreTitle(boolean isPlaying, boolean isPaused) {
        if(isPaused)
            return "Paused";
        else if(isPlaying)
            return "Playing";
        else
            return "Stopped";
    }

    private static Color genColor(boolean isPlaying,boolean isPaused) {
        if(isPaused)
            return Color.YELLOW;
        else if(isPlaying)
            return Color.GREEN;
        else
            return Color.RED;
    }

    private static String genTitle(GuildMusicManager musicManager) {
        if(musicManager.getQueManager().getQueue().isEmpty())
            return genPreTitle(false,false);
        return genPreTitle(true,musicManager.isPaused()) + ": " + musicManager.getQueManager().getQueue().get(0).track().getInfo().getTitle();
    }

    private static String getImageURL(GuildMusicManager musicManager) {
        return musicManager.getQueManager().getQueue().get(0).track().getInfo().getArtworkUrl();
    }

    private static String getArtist(GuildMusicManager musicManager) {
        return musicManager.getQueManager().getQueue().get(0).track().getInfo().getAuthor();
    }

    private static String getTitle(GuildMusicManager musicManager) {
        return musicManager.getQueManager().getQueue().get(0).track().getInfo().getTitle();
    }


}
