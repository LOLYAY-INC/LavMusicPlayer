package io.lolyay.embedmakers;

import io.lolyay.musicbot.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;

public class StatusEmbedGenerator {
    public static EmbedBuilder generate(GuildMusicManager musicManager) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(genTitle(musicManager));
        builder.addField("","**" + getTitle(musicManager) + "** by **" + getArtist(musicManager) + "**",false);
        builder.addField("",getPosition(musicManager) + " of " + getLength(musicManager),false);
        builder.setThumbnail(getImageURL(musicManager));
        return builder;
    }


    private static String genPreTitle(boolean isPlaying,boolean isPaused) {
        if(isPaused)
            return "Paused";
        else if(isPlaying)
            return "Playing";
        else
            return "Stopped";
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

    private static long getLength(GuildMusicManager musicManager) {
        return musicManager.getQueManager().getQueue().get(0).track().getInfo().getLength();
    }

    private static long getPosition(GuildMusicManager musicManager) {
        long position = 0;
        try {
            position = musicManager.getPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return position;
    }

}
