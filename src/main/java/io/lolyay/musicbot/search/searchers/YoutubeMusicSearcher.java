package io.lolyay.musicbot.search.searchers;

import io.lolyay.musicbot.search.AbstractSearcher;

public class YoutubeMusicSearcher extends AbstractSearcher {

    @Override
    public boolean canSearch(String query) {
        return query.startsWith("ytmsearch:") ||
                query.contains("music.youtube.com/watch");
    }

    @Override
    public String getPrefix() {
        return "ytmsearch:";
    }

    @Override
    public String getSourceName() {
        return "Youtube Music";
    }

}
