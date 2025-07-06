package io.lolyay.musicbot.search;


public abstract class AbstractSearcher {

    public abstract boolean canSearch(String query);

    public abstract String getSourceName();

    public abstract String getPrefix(); // eg. "ytsearch:"



}
