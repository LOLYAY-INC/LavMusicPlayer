package io.lolyay.musicbot.backendswapper.lavaplayer.search;

import io.lolyay.JdaMain;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.backendswapper.lavaplayer.LavaLinkPlayerManager;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.search.searchers.DefaultSearcher;
import io.lolyay.musicbot.search.searchers.HttpSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeMusicSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeSearcher;
import net.dv8tion.jda.api.entities.Member;

import java.util.Optional;
import java.util.function.Consumer;

public class LavaYoutubeSearcher {
    private static LavaSearchManager manager;

    public static void doSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, GuildMusicManager guildMusicManager, LavaSearchManager searchManager) {
        manager = searchManager;
        if (query.startsWith("http"))
            doFirstSearch(query, member, callback, guildId, guildMusicManager);
        else
            doSecondSearch(query, member, callback, guildId, guildMusicManager);
    }

    private static void doFirstSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, GuildMusicManager guildMusicManager) {
        HttpSearcher searcher = new HttpSearcher(guildMusicManager);
        if (!searcher.canSearch(query))
            doSecondSearch(query, member, callback, guildId, guildMusicManager);
        else {
            ((LavaLinkPlayerManager) JdaMain.playerManager).getAudioPlayerManager()
                    .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandler(
                            search -> {
                                if (!search.result().isSuccess()) {
                                    doSecondSearch(query, member, callback, guildId, guildMusicManager);
                                } else {
                                    callback.accept(search);
                                }
                            }, query, guildId, member, searcher));
        }

    }

    private static void doSecondSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, GuildMusicManager guildMusicManager) {
        YoutubeMusicSearcher searcher = new YoutubeMusicSearcher(guildMusicManager);
        ((LavaLinkPlayerManager) JdaMain.playerManager).getAudioPlayerManager()
                .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandler(
                        search -> {
                            if (!search.result().isSuccess()) {
                                doThirdSearch(query, member, callback, guildId, guildMusicManager);
                            } else {
                                callback.accept(search);
                            }
                        }, query, guildId, member, searcher));


    }

    private static void doThirdSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, GuildMusicManager guildMusicManager) {
        YoutubeSearcher searcher = new YoutubeSearcher(guildMusicManager);

        ((LavaLinkPlayerManager) JdaMain.playerManager).getAudioPlayerManager()
                .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandler(
                        search -> {
                            if (!search.result().isSuccess()) {
                                doFourthSearch(query, member, callback, guildId, guildMusicManager);
                            } else {
                                callback.accept(search);
                            }
                        }, query, guildId, member, searcher));


    }

    private static void doFourthSearch(String query, Optional<Member> member, Consumer<Search> callback, long guildId, GuildMusicManager guildMusicManager) {
        DefaultSearcher searcher = new DefaultSearcher(guildMusicManager);
        ((LavaLinkPlayerManager) JdaMain.playerManager).getAudioPlayerManager()
                .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandler(callback, query, guildId, member, searcher));
    }
}
