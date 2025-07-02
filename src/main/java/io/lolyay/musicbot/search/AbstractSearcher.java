package io.lolyay.musicbot.search;

import io.lolyay.musicbot.GuildMusicManager;

public abstract class AbstractSearcher {
    private final GuildMusicManager guildMusicManager;

    public AbstractSearcher(GuildMusicManager guildMusicManager) {
        this.guildMusicManager = guildMusicManager;
    }

    protected GuildMusicManager getGuildMusicManager() {
        return guildMusicManager;
    }


    public abstract boolean canSearch(String query);

    public abstract String getSourceName();

    public abstract String getPrefix(); // eg. "ytsearch:"



}
