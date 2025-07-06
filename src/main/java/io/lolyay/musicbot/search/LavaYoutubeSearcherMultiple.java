package io.lolyay.musicbot.search;

import io.lolyay.JdaMain;
import io.lolyay.musicbot.LavaLinkPlayerManager;
import io.lolyay.musicbot.search.searchers.DefaultSearcher;
import io.lolyay.musicbot.search.searchers.HttpSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeMusicSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeSearcher;

import java.util.function.Consumer;

public class LavaYoutubeSearcherMultiple {
    private static LavaSearchManager manager;

    public static void doSearch(String query, Consumer<Search[]> callback, LavaSearchManager searchManager) {
        manager = searchManager;
        if (query.startsWith("http"))
            doFirstSearch(query, callback);
        else
            doSecondSearch(query, callback);
    }

    private static void doFirstSearch(String query, Consumer<Search[]> callback) {
        HttpSearcher searcher = new HttpSearcher();
        if (!searcher.canSearch(query))
            doSecondSearch(query, callback);
        else {
            ((LavaLinkPlayerManager) JdaMain.playerManager).getAudioPlayerManager()
                    .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandlerMultiple(
                            search -> {
                                for (Search s : search) {
                                    if (!s.result().isSuccess()) {
                                        doSecondSearch(query, callback);
                                        return;
                                    }
                                }
                                callback.accept(search);
                            }, query, searcher));
        }

    }

    private static void doSecondSearch(String query, Consumer<Search[]> callback) {
        YoutubeMusicSearcher searcher = new YoutubeMusicSearcher();
        ((LavaLinkPlayerManager) JdaMain.playerManager).getAudioPlayerManager()
                .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandlerMultiple(
                        search -> {
                            for (Search s : search) {
                                if (!s.result().isSuccess()) {
                                    doThirdSearch(query, callback);
                                    return;
                                }
                            }
                            callback.accept(search);

                        }, query, searcher));


    }

    private static void doThirdSearch(String query, Consumer<Search[]> callback) {
        YoutubeSearcher searcher = new YoutubeSearcher( );

        ((LavaLinkPlayerManager) JdaMain.playerManager).getAudioPlayerManager()
                .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandlerMultiple(
                        search -> {
                            for (Search s : search) {
                                if (!s.result().isSuccess()) {
                                    doFourthSearch(query, callback);
                                    return;
                                }
                            }
                            callback.accept(search);
                        }, query, searcher));


    }

    private static void doFourthSearch(String query, Consumer<Search[]> callback) {
        DefaultSearcher searcher = new DefaultSearcher();
        ((LavaLinkPlayerManager) JdaMain.playerManager).getAudioPlayerManager()
                .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandlerMultiple(callback, query, searcher));
    }
}
