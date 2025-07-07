package io.lolyay.search.searchers;

import io.lolyay.search.AbstractSearcher;

public class HttpSearcher extends AbstractSearcher {

    @Override
    public boolean canSearch(String query) {
        return query.startsWith("http://") || query.startsWith("https://");
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public String getSourceName() {
        return "Internet";
    }

}
