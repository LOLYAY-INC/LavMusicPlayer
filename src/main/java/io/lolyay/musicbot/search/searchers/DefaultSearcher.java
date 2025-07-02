package io.lolyay.musicbot.search.searchers;

import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.search.AbstractSearcher;

public class DefaultSearcher extends AbstractSearcher {
    public DefaultSearcher(GuildMusicManager guildMusicManager) {
        super(guildMusicManager);
    }

    // a default searcher that assumes that every "xyz:query" is a valid search


    @Override
    public boolean canSearch(String query) {
        return query.contains(":");
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public String getSourceName() {
        return "Other Sources";
    }

}
