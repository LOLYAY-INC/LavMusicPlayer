package io.lolyay.musicbot.backendswapper.client.search;

import dev.arbjerg.lavalink.client.FunctionalLoadResultHandler;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.*;
import dev.arbjerg.lavalink.protocol.v4.LoadResult;
import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.customevents.events.search.PlaylistTrackLoadedEvent;
import io.lolyay.customevents.events.search.PreSearchEvent;
import io.lolyay.customevents.events.search.SearchNotFoundEvent;
import io.lolyay.customevents.events.search.SingleTrackLoadedEvent;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.RequestorData;
import io.lolyay.musicbot.backendswapper.AbstractSearchManager;
import io.lolyay.musicbot.backendswapper.client.ClientPlayerManager;
import io.lolyay.musicbot.search.AbstractSearcher;
import io.lolyay.musicbot.search.PlaylistData;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.search.searchers.DefaultSearcher;
import io.lolyay.musicbot.search.searchers.HttpSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeMusicSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeSearcher;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Logger;
import kotlinx.serialization.json.JsonObject;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ClientSearchManager extends AbstractSearchManager {

    // Default Search Order:
    // Internet (http) -> Youtube Music (ytm) -> Youtube (yt) -> Other (other)
    private final Class<? extends AbstractSearcher>[] searchers = new Class[]{
            HttpSearcher.class,
            YoutubeMusicSearcher.class,
            YoutubeSearcher.class,
            DefaultSearcher.class
    };

    public ClientSearchManager(long guildId) {
        super(guildId);
    }

    public void searchWithDefaultOrder(String query, Optional<Member> member, Consumer<Search> callback, long guildId) {
        Link client = ((ClientPlayerManager) JdaMain.playerManager).lavaLinkClient.getOrCreateLink(guildId);
        GuildMusicManager guildMusicManager = JdaMain.playerManager.getGuildMusicManager(guildId);

        PreSearchEvent.SearchEventResult override = JdaMain.eventBus.postAndGet(new PreSearchEvent(guildId, query, callback, member.orElse(null))).getOverride();

        if (override != null) {
            Logger.debug("Search override: " + override.getStatus());
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

        ClientYoutubeSearcher.doSearch(query, member, callback, guildId, client, guildMusicManager, this);

    }


    public void handleLoadResult(AbstractSearcher source, LavalinkLoadResult loadResult, Optional<Member> member, Consumer<Search> callback, String query, long guildId) {
        new FunctionalLoadResultHandler(
                trackLoaded -> handleTrackLoadedResult(source, trackLoaded, member, callback, query, guildId),
                playlistloaded -> handlePlaylistLoadedResult(source, playlistloaded, member, callback, query, guildId),
                searchResult -> handleSearchResult(source, searchResult, member, callback, query, guildId),
                () -> handleTrackNotFound(source, member, callback, query, guildId),
                error -> handleTrackError(source, error, member, callback, query, guildId)


        ).accept(loadResult);

    }


    private void handleTrackLoadedResult(AbstractSearcher source, TrackLoaded trackLoaded, Optional<Member> member, Consumer<Search> callback, String query, long guildId) {
        Logger.log("Loaded single track from " + source.getSourceName() + ": " + trackLoaded.getTrack().getInfo().getTitle());
        MusicAudioTrack musicAudioTrack = MusicAudioTrack.fromTrack(trackLoaded.getTrack(), RequestorData.fromMember(member, guildId));

        SingleTrackLoadedEvent event = JdaMain.eventBus.postAndGet(new SingleTrackLoadedEvent(callback, source, guildId, member, musicAudioTrack));

        event.getCallback().accept(Search.wasTrack(
                Search.SearchResult.SUCCESS(), source.getSourceName(),
                query,
                musicAudioTrack
        ));

    }

    private void handlePlaylistLoadedResult(AbstractSearcher source, PlaylistLoaded playlistLoaded, Optional<Member> member, Consumer<Search> callback, String query, long guildId) {
        if (ConfigManager.getConfigBool("add-full-playlists-to-queue")) {
            Logger.debug("Treating Playlist as full playlist from " + source.getSourceName() + ": " + playlistLoaded.getInfo().getName());
            RequestorData userData = RequestorData.fromMember(member, guildId);

            PlaylistData playlistData = PlaylistData.fromTracksAndInfo(playlistLoaded.getTracks(), playlistLoaded.getInfo(), userData);

            JdaMain.eventBus.post(new PlaylistTrackLoadedEvent(callback, source, guildId, member, playlistData.tracks()));

            callback.accept(Search.wasPlaylist(
                    Search.SearchResult.PLAYLIST(),
                    source.getSourceName(),
                    query,
                    playlistData
            ));


        } else {

            // No event because it will call handleTrackLoadedResult

            Logger.debug("Treating Playlist as single track from " + source.getSourceName() + ": " + playlistLoaded.getInfo().getName());
            Track selectedTrack = playlistLoaded.getTracks().get(playlistLoaded.getInfo().getSelectedTrack());
            var trackV4 = new dev.arbjerg.lavalink.protocol.v4.Track(
                    selectedTrack.getEncoded(),
                    selectedTrack.getInfo(),
                    new JsonObject(Map.of()), // too much to implement actual pluginInfo even tho it exists
                    new JsonObject(Map.of()) // same here, we dont need userdata here only trackdata, would be good if we actually found out how to convert a JsonNode into a JsonObject
            );
            TrackLoaded trackLoaded = new TrackLoaded(new LoadResult.TrackLoaded(trackV4));
            handleTrackLoadedResult(source, trackLoaded, member, callback, query, guildId);
        }


    }

    private void handleTrackNotFound(AbstractSearcher source, Optional<Member> member, Consumer<Search> callback, String query, long guildId) {

        JdaMain.eventBus.post(new SearchNotFoundEvent(guildId, query, callback, member.orElse(null), source));

        Logger.debug("Track not found from " + source.getSourceName() + ": " + query);

        callback.accept(Search.wasNotFound(
                Search.SearchResult.NOT_FOUND(),
                source.getSourceName(),
                query
        ));


    }

    private void handleSearchResult(AbstractSearcher source, SearchResult searchResult, Optional<Member> member, Consumer<Search> callback, String query, long guildId) {

        Logger.debug("Search result from " + source.getSourceName() + ": " + query);

        // No event because it will call handleTrackLoadedResult

        Track selectedTrack = searchResult.getTracks().getFirst();
        var trackV4 = new dev.arbjerg.lavalink.protocol.v4.Track(
                selectedTrack.getEncoded(),
                selectedTrack.getInfo(),
                new JsonObject(Map.of()), // too much to implement actual pluginInfo even tho it exists
                new JsonObject(Map.of()) // same here, we dont need userdata here only trackdata, would be good if we actually found out how to convert a JsonNode into a JsonObject
        );
        TrackLoaded trackLoaded = new TrackLoaded(new LoadResult.TrackLoaded(trackV4));
        handleTrackLoadedResult(source, trackLoaded, member, callback, query, guildId);
    }


    private void handleTrackError(AbstractSearcher source, LoadFailed error, Optional<Member> member, Consumer<Search> callback, String query, long guildId) {

        Logger.err("Error loading track from " + source.getSourceName() + ": " + error.getException().getMessage());

        JdaMain.eventBus.post(new SearchNotFoundEvent(-1, query, callback, member.orElse(null), source));

        callback.accept(Search.wasError(
                Search.SearchResult.ERROR(error.getException().getMessage()),
                source.getSourceName(),
                query
        ));
    }

}
