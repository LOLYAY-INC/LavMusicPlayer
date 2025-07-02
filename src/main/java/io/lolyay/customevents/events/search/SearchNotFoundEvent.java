package io.lolyay.customevents.events.search;

import io.lolyay.customevents.Event;
import io.lolyay.musicbot.search.AbstractSearcher;
import io.lolyay.musicbot.search.Search;
import net.dv8tion.jda.api.entities.Member;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class SearchNotFoundEvent extends Event {
    private final long guildId;
    private final String query;
    private final Optional<Member> member;
    private final AbstractSearcher searcher;
    private final Consumer<Search> successCallback;

    public SearchNotFoundEvent(long guildId, String query, Consumer<Search> successCallback, @Nullable Member member, AbstractSearcher searcher) {
        this.guildId = guildId;
        this.searcher = searcher;
        this.query = query;
        this.successCallback = successCallback;
        this.member = Optional.ofNullable(member);
    }

    public long getGuildId() {
        return guildId;
    }

    public String getQuery() {
        return query;
    }

    public Consumer<Search> getSuccessCallback() {
        return successCallback;
    }

    public Optional<Member> getMember() {
        return member;
    }


}
