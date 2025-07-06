package io.lolyay.musicbot.search;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.customevents.events.search.PlaylistTrackLoadedEvent;
import io.lolyay.customevents.events.search.PreSearchEvent;
import io.lolyay.customevents.events.search.SearchNotFoundEvent;
import io.lolyay.customevents.events.search.SingleTrackLoadedEvent;
import io.lolyay.musicbot.abstracts.AbstractSearchManager;
import io.lolyay.musicbot.search.searchers.DefaultSearcher;
import io.lolyay.musicbot.search.searchers.HttpSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeMusicSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeSearcher;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Logger;

import java.util.function.Consumer;

public class LavaSearchManager extends AbstractSearchManager {

    // Default Search Order:
    // Internet (http) -> Youtube Music (ytm) -> Youtube (yt) -> Other (other)
    private final Class<? extends AbstractSearcher>[] searchers = new Class[]{
            HttpSearcher.class,
            YoutubeMusicSearcher.class,
            YoutubeSearcher.class,
            DefaultSearcher.class
    };


    public void searchWithDefaultOrder(String query, Consumer<Search> callback) {

        PreSearchEvent.SearchEventResult override = JdaMain.eventBus.postAndGet(new PreSearchEvent(query, callback)).getOverride();

        if (override != null) {
            Logger.debug("Search override: " + override.getStatus());
            switch (override.getStatus()) {
                case NOT_FOUND -> {
                    callback.accept(Search.wasNotFound(Search.SearchResult.NOT_FOUND("Search was not found (override)"), "System", query));
                    return;
                }
                case FOUND -> {
                    MusicAudioTrack track = override.getTrack();
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

        LavaYoutubeSearcher.doSearch(query, callback, this);

    }

    public void searchWithDefaultOrderMultiple(String query, Consumer<Search[]> callback) {

        LavaYoutubeSearcherMultiple.doSearch(query, callback, this);

    }

    protected static class LavaResultHandler implements AudioLoadResultHandler {
        private final Consumer<Search> callback;
        private final String query;
        private final AbstractSearcher source;

        public LavaResultHandler(Consumer<Search> callback, String query, AbstractSearcher source) {
            this.callback = callback;
            this.query = query;
            this.source = source;
        }

        @Override
        public void trackLoaded(AudioTrack audioTrack) {
            Logger.log("Loaded single track from " + source.getSourceName() + ": " + audioTrack.getInfo().title);
            MusicAudioTrack musicAudioTrack = new MusicAudioTrack(audioTrack);

            SingleTrackLoadedEvent event = JdaMain.eventBus.postAndGet(new SingleTrackLoadedEvent(callback, source, musicAudioTrack));

            event.getCallback().accept(Search.wasTrack(
                    Search.SearchResult.SUCCESS(), source.getSourceName(),
                    query,
                    musicAudioTrack
            ));
        }

        @Override
        public void playlistLoaded(AudioPlaylist audioPlaylist) {
            if (audioPlaylist.isSearchResult()) {
                trackSearch(audioPlaylist);
                return;
            }


                // No event because it will call handleTrackLoadedResult

            Logger.debug("Treating Playlist as single track from " + source.getSourceName() + ": " + audioPlaylist.getName());
            trackLoaded(audioPlaylist.getSelectedTrack() == null ? audioPlaylist.getTracks().getFirst() : audioPlaylist.getSelectedTrack());

        }

        @Override
        public void noMatches() {
            JdaMain.eventBus.post(new SearchNotFoundEvent(query, callback, source));

            Logger.debug("Track not found from " + source.getSourceName() + ": " + query);

            callback.accept(Search.wasNotFound(
                    Search.SearchResult.NOT_FOUND(),
                    source.getSourceName(),
                    query
            ));
        }

        @Override
        public void loadFailed(FriendlyException e) {
            Logger.err("Error loading track from " + source.getSourceName() + ": " + e.getMessage());

            JdaMain.eventBus.post(new SearchNotFoundEvent(query, callback, source));

            callback.accept(Search.wasError(
                    Search.SearchResult.ERROR(e.getMessage()),
                    source.getSourceName(),
                    query
            ));
        }

        private void trackSearch(AudioPlaylist audioPlaylist) {

            // here for future search selection???
            Logger.debug("Search result from " + source.getSourceName() + ": " + query);

            // No event because it will call handleTrackLoadedResult

            trackLoaded(audioPlaylist.getSelectedTrack() == null ? audioPlaylist.getTracks().getFirst() : audioPlaylist.getSelectedTrack());
        }
    }


    protected static class LavaResultHandlerMultiple implements AudioLoadResultHandler {
        private final Consumer<Search[]> callback;
        private final String query;
        private final AbstractSearcher source;

        public LavaResultHandlerMultiple(Consumer<Search[]> callback, String query, AbstractSearcher source) {
            this.callback = callback;
            this.query = query;
            this.source = source;
        }

        @Override
        public void trackLoaded(AudioTrack audioTrack) {
            Logger.log("Loaded single track from " + source.getSourceName() + ": " + audioTrack.getInfo().title);
            MusicAudioTrack musicAudioTrack = new MusicAudioTrack(audioTrack);

            JdaMain.eventBus.post(new SingleTrackLoadedEvent(null, source, musicAudioTrack));

            callback.accept(new Search[]{Search.wasTrack(
                    Search.SearchResult.SUCCESS(), source.getSourceName(),
                    query,
                    musicAudioTrack
            )});
        }

        @Override
        public void playlistLoaded(AudioPlaylist audioPlaylist) {
            if (audioPlaylist.isSearchResult()) {
                trackSearch(audioPlaylist);
                return;
            }


            // No event because it will call handleTrackLoadedResult

            Logger.debug("Treating Playlist as single track from " + source.getSourceName() + ": " + audioPlaylist.getName());
            trackLoaded(audioPlaylist.getSelectedTrack() == null ? audioPlaylist.getTracks().getFirst() : audioPlaylist.getSelectedTrack());

        }

        @Override
        public void noMatches() {
        //    JdaMain.eventBus.post(new SearchNotFoundEvent(query, callback, source));

            Logger.debug("Track not found from " + source.getSourceName() + ": " + query);

            callback.accept(new Search[]{Search.wasNotFound(
                    Search.SearchResult.NOT_FOUND(),
                    source.getSourceName(),
                    query
            )});
        }

        @Override
        public void loadFailed(FriendlyException e) {
            Logger.err("Error loading track from " + source.getSourceName() + ": " + e.getMessage());

           // JdaMain.eventBus.post(new SearchNotFoundEvent(query, callback, source));

            callback.accept(new Search[]{Search.wasError(
                    Search.SearchResult.ERROR(e.getMessage()),
                    source.getSourceName(),
                    query
            )});
        }

        private void trackSearch(AudioPlaylist audioPlaylist) {

            // here for future search selection???
            Logger.debug("Search result from " + source.getSourceName() + ": " + query);

            // No event because it will call handleTrackLoadedResult

            MusicAudioTrack[] tracks = new MusicAudioTrack[audioPlaylist.getTracks().size()];
            for (int i = 0; i < audioPlaylist.getTracks().size(); i++) {
                tracks[i] = new MusicAudioTrack(audioPlaylist.getTracks().get(i));
            }
            Search[] searches = new Search[tracks.length];
            for (int i = 0; i < tracks.length; i++) {
                searches[i] = Search.wasTrack(
                        Search.SearchResult.SUCCESS(), source.getSourceName(),
                        query,
                        tracks[i]
                );
            }


            callback.accept(searches);


        }
    }
}
