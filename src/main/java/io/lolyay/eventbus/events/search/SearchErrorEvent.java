package io.lolyay.eventbus.events.search;

import io.lolyay.eventbus.Event;
import io.lolyay.search.AbstractSearcher;
import io.lolyay.search.Search;

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
