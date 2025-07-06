package io.lolyay.customevents.events.search;

import io.lolyay.customevents.Event;
import io.lolyay.musicbot.search.AbstractSearcher;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

import java.util.Optional;
import java.util.function.Consumer;

public class SingleTrackLoadedEvent extends Event {
    private final AbstractSearcher searcher;
    private Consumer<Search> callback;
    private MusicAudioTrack track;

    public SingleTrackLoadedEvent(Consumer<Search> callback, AbstractSearcher searcher, MusicAudioTrack track) {
        this.callback = callback;
        this.searcher = searcher;
        this.track = track;
    }

    public MusicAudioTrack getTrack() {
        return track;
    }

    public void setTrack(MusicAudioTrack track) {
        this.track = track;
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
