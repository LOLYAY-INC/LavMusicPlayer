package io.lolyay.music.abstracts;

import io.lolyay.search.Search;

import java.util.function.Consumer;

public abstract class AbstractSearchManager {
    public abstract void searchWithDefaultOrder(String query, Consumer<Search> callback);


}
