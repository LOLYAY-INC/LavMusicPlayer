package io.lolyay.musicbot.backendswapper.client.search;

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

public class ClientYoutubeSearcher {
    private static ClientSearchManager manager;

    public static void doSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, Link link, GuildMusicManager guildMusicManager, ClientSearchManager searchManager) {
        manager = searchManager;
        if (query.startsWith("http"))
            doFirstSearch(query, member, callback, guildId, link, guildMusicManager);
        else
            doSecondSearch(query, member, callback, guildId, link, guildMusicManager);
    }

    private static void doFirstSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, Link link, GuildMusicManager guildMusicManager) {
        HttpSearcher searcher = new HttpSearcher(guildMusicManager);
        if (!searcher.canSearch(query))
            doSecondSearch(query, member, callback, guildId, link, guildMusicManager);
        else {
            link.loadItem(searcher.getPrefix() + query).subscribe(lavalinkLoadResult -> manager.handleLoadResult(
                    searcher,
                    lavalinkLoadResult,
                    member,
                    search -> {
                        if (!search.result().isSuccess()) {
                            doSecondSearch(query, member, callback, guildId, link, guildMusicManager);
                        } else {
                            callback.accept(search);
                        }
                    },
                    query,
                    guildId
            ));
        }

    }

    private static void doSecondSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, Link link, GuildMusicManager guildMusicManager) {
        YoutubeMusicSearcher searcher = new YoutubeMusicSearcher(guildMusicManager);
        if (!searcher.canSearch(query))
            doThirdSearch(query, member, callback, guildId, link, guildMusicManager);
        else {
            link.loadItem(searcher.getPrefix() + query).subscribe(lavalinkLoadResult -> manager.handleLoadResult(
                    searcher,
                    lavalinkLoadResult,
                    member,
                    search -> {
                        if (!search.result().isSuccess()) {
                            doThirdSearch(query, member, callback, guildId, link, guildMusicManager);
                        } else {
                            callback.accept(search);
                        }
                    },
                    query,
                    guildId
            ));
        }

    }

    private static void doThirdSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, Link link, GuildMusicManager guildMusicManager) {
        YoutubeSearcher searcher = new YoutubeSearcher(guildMusicManager);
        if (!searcher.canSearch(query))
            doFourthSearch(query, member, callback, guildId, link, guildMusicManager);
        else {
            link.loadItem(searcher.getPrefix() + query).subscribe(lavalinkLoadResult -> manager.handleLoadResult(
                    searcher,
                    lavalinkLoadResult,
                    member,
                    search -> {
                        if (!search.result().isSuccess()) {
                            doFourthSearch(query, member, callback, guildId, link, guildMusicManager);
                        } else {
                            callback.accept(search);
                        }
                    },
                    query,
                    guildId
            ));
        }

    }

    private static void doFourthSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, Link link, GuildMusicManager guildMusicManager) {
        DefaultSearcher searcher = new DefaultSearcher(guildMusicManager);
        link.loadItem(searcher.getPrefix() + query).subscribe(lavalinkLoadResult -> manager.handleLoadResult(
                searcher,
                lavalinkLoadResult,
                member,
                callback,
                query,
                guildId
        ));
    }
}
