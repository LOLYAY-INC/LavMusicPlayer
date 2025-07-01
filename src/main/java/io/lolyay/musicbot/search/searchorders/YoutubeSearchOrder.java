package io.lolyay.musicbot.search.searchorders;

import dev.arbjerg.lavalink.client.Link;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.search.searchers.DefaultSearcher;
import io.lolyay.musicbot.search.searchers.HttpSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeMusicSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeSearcher;
import net.dv8tion.jda.api.entities.Member;

import java.util.Optional;
import java.util.function.Consumer;

public class YoutubeSearchOrder {
    public static void doSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, Link link, GuildMusicManager guildMusicManager) {
        if (query.startsWith("http"))
            doFirstSearch(query, member, callback, guildId, link, guildMusicManager);
        else
            doSecondSearch(query, member, callback, guildId, link, guildMusicManager);
    }

    private static void doFirstSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, Link link, GuildMusicManager guildMusicManager) {
        new HttpSearcher(link, guildMusicManager).search(query, member, search -> {
            if (!search.result().isSuccess()) {
                doSecondSearch(query, member, callback, guildId, link, guildMusicManager);
            } else {
                callback.accept(search);
            }
        });
    }

    private static void doSecondSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, Link link, GuildMusicManager guildMusicManager) {
        new YoutubeMusicSearcher(link, guildMusicManager).search("ytmsearch:" + query, member, search -> {
            if (!search.result().isSuccess()) {
                doThirdSearch(query, member, callback, guildId, link, guildMusicManager);
            } else {
                callback.accept(search);
            }
        });
    }

    private static void doThirdSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, Link link, GuildMusicManager guildMusicManager) {
        new YoutubeSearcher(link, guildMusicManager).search("ytsearch:" + query, member, search -> {
            if (!search.result().isSuccess() && query.contains(":")) {
                doFourthSearch(query, member, callback, guildId, link, guildMusicManager);
            } else {
                callback.accept(search);
            }
        });

    }

    private static void doFourthSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, Link link, GuildMusicManager guildMusicManager) {
        new DefaultSearcher(link, guildMusicManager).search(query, member, callback);
    }
}
