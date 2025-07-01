package io.lolyay.musicbot.search.searchers;

import dev.arbjerg.lavalink.client.FunctionalLoadResultHandler;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.*;
import dev.arbjerg.lavalink.protocol.v4.LoadResult;
import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.customevents.events.search.PlaylistTrackLoadedEvent;
import io.lolyay.customevents.events.search.SearchNotFoundEvent;
import io.lolyay.customevents.events.search.SingleTrackLoadedEvent;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.RequestorData;
import io.lolyay.musicbot.search.PlaylistData;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Logger;
import kotlinx.serialization.json.JsonObject;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractSearcher {
    private final Link link;
    private final GuildMusicManager guildMusicManager;

    public AbstractSearcher(Link link, GuildMusicManager guildMusicManager) {
        this.link = link;
        this.guildMusicManager = guildMusicManager;
    }

    protected GuildMusicManager getGuildMusicManager() {
        return guildMusicManager;
    }

    protected Link getLink() {
        return link;
    }

    public abstract boolean canSearch(String query);

    public abstract String getSourceName();

    public abstract String getPrefix(); // eg. "ytsearch:"

    public abstract void search(String query, Optional<Member> member, Consumer<Search> callback);

    protected void handleLoadResult(LavalinkLoadResult loadResult, Optional<Member> member, Consumer<Search> callback, String query) {
        new FunctionalLoadResultHandler(
                trackLoaded -> handleTrackLoadedResult(trackLoaded, member, callback, query),
                playlistloaded -> handlePlaylistLoadedResult(playlistloaded, member, callback, query),
                searchResult -> handleSearchResult(searchResult, member, callback, query),
                () -> handleTrackNotFound(member, callback, query),
                error -> handleTrackError(error, member, callback, query)


        ).accept(loadResult);

    }

    private void handleTrackLoadedResult(TrackLoaded trackLoaded, Optional<Member> member, Consumer<Search> callback, String query) {
        Logger.log("Loaded single track from " + getSourceName() + ": " + trackLoaded.getTrack().getInfo().getTitle());
        MusicAudioTrack musicAudioTrack = MusicAudioTrack.fromTrack(trackLoaded.getTrack(), RequestorData.fromMember(member, link.getGuildId()));

        SingleTrackLoadedEvent event = JdaMain.eventBus.postAndGet(new SingleTrackLoadedEvent(callback, this, link.getGuildId(), member, musicAudioTrack));

        event.getCallback().accept(Search.wasTrack(
                Search.SearchResult.SUCCESS(), getSourceName(),
                query,
                musicAudioTrack
        ));

    }

    private void handlePlaylistLoadedResult(PlaylistLoaded playlistLoaded, Optional<Member> member, Consumer<Search> callback, String query) {
        if (ConfigManager.getConfigBool("add-full-playlists-to-queue")) {
            Logger.debug("Treating Playlist as full playlist from " + getSourceName() + ": " + playlistLoaded.getInfo().getName());
            RequestorData userData = RequestorData.fromMember(member, link.getGuildId());

            PlaylistData playlistData = PlaylistData.fromTracksAndInfo(playlistLoaded.getTracks(), playlistLoaded.getInfo(), userData);

            JdaMain.eventBus.post(new PlaylistTrackLoadedEvent(callback, this, link.getGuildId(), member, playlistData.tracks()));

            callback.accept(Search.wasPlaylist(
                    Search.SearchResult.PLAYLIST(),
                    getSourceName(),
                    query,
                    playlistData
            ));


        } else {

            // No event because it will call handleTrackLoadedResult

            Logger.debug("Treating Playlist as single track from " + getSourceName() + ": " + playlistLoaded.getInfo().getName());
            Track selectedTrack = playlistLoaded.getTracks().get(playlistLoaded.getInfo().getSelectedTrack());
            var trackV4 = new dev.arbjerg.lavalink.protocol.v4.Track(
                    selectedTrack.getEncoded(),
                    selectedTrack.getInfo(),
                    new JsonObject(Map.of()), // too much to implement actual pluginInfo even tho it exists
                    new JsonObject(Map.of()) // same here, we dont need userdata here only trackdata, would be good if we actually found out how to convert a JsonNode into a JsonObject
            );
            TrackLoaded trackLoaded = new TrackLoaded(new LoadResult.TrackLoaded(trackV4));
            handleTrackLoadedResult(trackLoaded, member, callback, query);
        }


    }

    private void handleTrackNotFound(Optional<Member> member, Consumer<Search> callback, String query) {

        JdaMain.eventBus.post(new SearchNotFoundEvent(getLink().getGuildId(), query, callback, member.orElse(null), this));

        Logger.debug("Track not found from " + getSourceName() + ": " + query);

        callback.accept(Search.wasNotFound(
                Search.SearchResult.NOT_FOUND(),
                getSourceName(),
                query
        ));


    }

    private void handleSearchResult(SearchResult searchResult, Optional<Member> member, Consumer<Search> callback, String query) {

        Logger.debug("Search result from " + getSourceName() + ": " + query);

        // No event because it will call handleTrackLoadedResult

        Track selectedTrack = searchResult.getTracks().getFirst();
        var trackV4 = new dev.arbjerg.lavalink.protocol.v4.Track(
                selectedTrack.getEncoded(),
                selectedTrack.getInfo(),
                new JsonObject(Map.of()), // too much to implement actual pluginInfo even tho it exists
                new JsonObject(Map.of()) // same here, we dont need userdata here only trackdata, would be good if we actually found out how to convert a JsonNode into a JsonObject
        );
        TrackLoaded trackLoaded = new TrackLoaded(new LoadResult.TrackLoaded(trackV4));
        handleTrackLoadedResult(trackLoaded, member, callback, query);
    }


    private void handleTrackError(LoadFailed error, Optional<Member> member, Consumer<Search> callback, String query) {

        Logger.err("Error loading track from " + getSourceName() + ": " + error.getException().getMessage());

        JdaMain.eventBus.post(new SearchNotFoundEvent(getLink().getGuildId(), query, callback, member.orElse(null), this));

        callback.accept(Search.wasError(
                Search.SearchResult.ERROR(error.getException().getMessage()),
                getSourceName(),
                query
        ));
    }


}
