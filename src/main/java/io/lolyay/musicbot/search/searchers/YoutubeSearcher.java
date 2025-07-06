package io.lolyay.musicbot.search.searchers;

import io.lolyay.musicbot.search.AbstractSearcher;

public class YoutubeSearcher extends AbstractSearcher {

    @Override
    public boolean canSearch(String query) {
        return query.startsWith("ytsearch:") ||
                query.contains("youtube.com/watch") ||
                query.contains("youtu.be/");
    }

    @Override
    public String getPrefix() {
        return "ytsearch:";
    }

    @Override
    public String getSourceName() {
        return "Youtube";
    }

}
