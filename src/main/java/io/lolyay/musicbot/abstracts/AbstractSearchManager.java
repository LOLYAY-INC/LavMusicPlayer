package io.lolyay.musicbot.abstracts;

import io.lolyay.musicbot.search.Search;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractSearchManager {
    public abstract void searchWithDefaultOrder(String query, Consumer<Search> callback);


}
