package io.lolyay.embedmakers;

import com.jagrosh.jlyrics.Lyrics;
import net.dv8tion.jda.api.EmbedBuilder;

public class LyricsEmbedGenerator {
    public static EmbedBuilder generate(Lyrics lyrics) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(lyrics.getTitle() + " by " + lyrics.getAuthor());
        builder.addField("", lyrics.getContent(), true);
        builder.setFooter(lyrics.getSource());
        return builder;
    }


}
