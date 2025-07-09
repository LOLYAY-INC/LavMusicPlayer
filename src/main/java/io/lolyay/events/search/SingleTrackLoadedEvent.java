package io.lolyay.events.search;

import io.lolyay.eventbus.Event;
import io.lolyay.search.AbstractSearcher;
import io.lolyay.search.Search;
import io.lolyay.music.track.MusicAudioTrack;

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
