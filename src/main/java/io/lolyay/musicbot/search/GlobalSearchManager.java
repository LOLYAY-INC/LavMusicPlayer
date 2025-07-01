package io.lolyay.musicbot.search;

import dev.arbjerg.lavalink.client.Link;
import io.lolyay.JdaMain;
import io.lolyay.customevents.events.search.PreSearchEvent;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.RequestorData;
import io.lolyay.musicbot.search.searchers.*;
import io.lolyay.musicbot.search.searchorders.YoutubeSearchOrder;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import net.dv8tion.jda.api.entities.Member;

import java.util.Optional;
import java.util.function.Consumer;

public class GlobalSearchManager {

    // Default Search Order:
    // Internet (http) -> Youtube Music (ytm) -> Youtube (yt) -> Other (other)
    private final Class<? extends AbstractSearcher>[] searchers = new Class[]{
            HttpSearcher.class,
            YoutubeMusicSearcher.class,
            YoutubeSearcher.class,
            DefaultSearcher.class
    };

    public void searchWithDefaultOrder(String query, Optional<Member> member, Consumer<Search> callback, long guildId) {
        Link client = JdaMain.lavalinkClient.getOrCreateLink(guildId);
        GuildMusicManager guildMusicManager = JdaMain.playerManager.getGuildMusicManager(guildId);

        PreSearchEvent.SearchEventResult override = JdaMain.eventBus.postAndGet(new PreSearchEvent(guildId, query, callback, member.orElse(null))).getOverride();

        if (override != null) {
            switch (override.getStatus()) {
                case NOT_FOUND -> {
                    callback.accept(Search.wasNotFound(Search.SearchResult.NOT_FOUND("Search was not found (override)"), "System", query));
                    return;
                }
                case FOUND -> {
                    MusicAudioTrack track = MusicAudioTrack.fromTrack(override.getTrack().track(), RequestorData.fromMember(member, guildId));
                    callback.accept(Search.wasTrack(Search.SearchResult.SUCCESS("Track was found (override)"), "System", query, track));
                    return;
                }
                case ERROR -> {
                    callback.accept(Search.wasError(Search.SearchResult.ERROR("Search failed (override)"), "System", query));
                    return;
                }
                case NO_OVERRIDE -> {
                    break;
                }
                case CANCELLED -> {
                    callback.accept(Search.wasError(Search.SearchResult.ERROR("Search was cancelled (override)"), "System", query));
                    return;
                }
            }
        }

        YoutubeSearchOrder.doSearch(query, member, callback, guildId, client, guildMusicManager);

    }


}
