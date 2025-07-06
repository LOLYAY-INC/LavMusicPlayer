package io.lolyay.customevents.events.search;

import io.lolyay.customevents.Event;
import io.lolyay.musicbot.search.AbstractSearcher;
import io.lolyay.musicbot.search.Search;

import java.util.Optional;
import java.util.function.Consumer;

public class SearchErrorEvent extends Event {
    private final String query;
    private final AbstractSearcher searcher;
    private final Consumer<Search> successCallback;

    public SearchErrorEvent(String query, Consumer<Search> successCallback, AbstractSearcher searcher) {
        this.searcher = searcher;
        this.query = query;
        this.successCallback = successCallback;
    }


    public String getQuery() {
        return query;
    }

    public Consumer<Search> getSuccessCallback() {
        return successCallback;
    }



}
