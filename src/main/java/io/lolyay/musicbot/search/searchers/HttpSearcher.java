package io.lolyay.musicbot.search.searchers;

import dev.arbjerg.lavalink.client.Link;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.search.Search;
import net.dv8tion.jda.api.entities.Member;

import java.util.Optional;
import java.util.function.Consumer;

public class HttpSearcher extends AbstractSearcher {
    public HttpSearcher(Link link, GuildMusicManager guildMusicManager) {
        super(link, guildMusicManager);
    }

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

    @Override
    public void search(String query, Optional<Member> member, Consumer<Search> callback) {
        if (!canSearch(query))
            callback.accept(Search.wasError(Search.SearchResult.ERROR("Source cant load this track, Wrong type!"), getSourceName(), query));
        getLink().loadItem(query).subscribe((loadResult) -> handleLoadResult(loadResult, member, callback, query));
    }
}
