package io.lolyay.search;

import io.lolyay.LavMusicPlayer;
import io.lolyay.music.lavalink.LavaLinkPlayerManager;
import io.lolyay.search.searchers.DefaultSearcher;
import io.lolyay.search.searchers.HttpSearcher;
import io.lolyay.search.searchers.YoutubeMusicSearcher;
import io.lolyay.search.searchers.YoutubeSearcher;

import java.util.function.Consumer;

public class LavaYoutubeSearcher {
    private static LavaSearchManager manager;

    public static void doSearch(String query, Consumer<Search> callback, LavaSearchManager searchManager) {
        manager = searchManager;
        if (query.startsWith("http"))
            doFirstSearch(query, callback);
        else
            doSecondSearch(query, callback);
    }

    private static void doFirstSearch(String query, Consumer<Search> callback) {
        HttpSearcher searcher = new HttpSearcher();
        if (!searcher.canSearch(query))
            doSecondSearch(query, callback);
        else {
            ((LavaLinkPlayerManager) LavMusicPlayer.playerManager).getAudioPlayerManager()
                    .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandler(
                            search -> {
                                if (!search.result().isSuccess()) {
                                    doSecondSearch(query, callback);
                                } else {
                                    callback.accept(search);
                                }
                            }, query, searcher));
        }

    }

    private static void doSecondSearch(String query, Consumer<Search> callback) {
        YoutubeMusicSearcher searcher = new YoutubeMusicSearcher();
        ((LavaLinkPlayerManager) LavMusicPlayer.playerManager).getAudioPlayerManager()
                .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandler(
                        search -> {
                            if (!search.result().isSuccess()) {
                                doThirdSearch(query, callback);
                            } else {
                                callback.accept(search);
                            }
                        }, query, searcher));


    }

    private static void doThirdSearch(String query, Consumer<Search> callback) {
        YoutubeSearcher searcher = new YoutubeSearcher( );

        ((LavaLinkPlayerManager) LavMusicPlayer.playerManager).getAudioPlayerManager()
                .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandler(
                        search -> {
                            if (!search.result().isSuccess()) {
                                doFourthSearch(query, callback);
                            } else {
                                callback.accept(search);
                            }
                        }, query, searcher));


    }

    private static void doFourthSearch(String query, Consumer<Search> callback) {
        DefaultSearcher searcher = new DefaultSearcher();
        ((LavaLinkPlayerManager) LavMusicPlayer.playerManager).getAudioPlayerManager()
                .loadItem(searcher.getPrefix() + query, new LavaSearchManager.LavaResultHandler(callback, query, searcher));
    }
}
