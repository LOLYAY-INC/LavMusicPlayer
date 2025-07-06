package io.lolyay.customevents.events.search;

import io.lolyay.customevents.Event;
import io.lolyay.musicbot.search.AbstractSearcher;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PlaylistTrackLoadedEvent extends Event {
    private final AbstractSearcher searcher;
    private Consumer<Search> callback;
    private List<MusicAudioTrack> tracks;

    public PlaylistTrackLoadedEvent(Consumer<Search> callback, AbstractSearcher searcher, List<MusicAudioTrack> tracks) {
        this.callback = callback;
        this.searcher = searcher;

        this.tracks = tracks;
    }

    public List<MusicAudioTrack> getTrack() {
        return tracks;
    }

    public void setTrack(List<MusicAudioTrack> tracks) {
        this.tracks = tracks;
    }

    public Consumer<Search> getCallback() {
        return callback;
    }

    public void setCallback(Consumer<Search> callback) {
        this.callback = callback;
    }

    public AbstractSearcher getSearcher() {
        return searcher;
    }
}
