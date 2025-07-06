package io.lolyay.customevents.events.search;

import io.lolyay.customevents.Event;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

import java.util.Optional;
import java.util.function.Consumer;

public class PreSearchEvent extends Event {
    private final String query;
    private final Consumer<Search> callback;
    private SearchEventResult override;

    public PreSearchEvent(String query, Consumer<Search> callback) {
        this.query = query;
        this.callback = callback;
    }

    public SearchEventResult getOverride() {
        return override;
    }

    public void overrideResult(SearchEventResult override) {
        this.override = override;
    }


    public String getQuery() {
        return query;
    }

    public Consumer<Search> getCallback() {
        return callback;
    }


    public enum SearchEventResultStatus {
        NOT_FOUND(1),
        ERROR(2),
        FOUND(3),
        CANCELLED(5),
        NO_OVERRIDE(4);
        public final int value;

        SearchEventResultStatus(int value) {
            this.value = value;
        }
    }

    public static class SearchEventResult {
        private final SearchEventResultStatus status;
        private final MusicAudioTrack track;

        private SearchEventResult(SearchEventResultStatus status, MusicAudioTrack track) {
            this.status = status;
            this.track = track;
        }

        public static SearchEventResult NOT_FOUND() {
            return new SearchEventResult(SearchEventResultStatus.NOT_FOUND, null);
        }

        public static SearchEventResult ERROR() {
            return new SearchEventResult(SearchEventResultStatus.ERROR, null);
        }

        public static SearchEventResult FOUND(MusicAudioTrack track) {
            return new SearchEventResult(SearchEventResultStatus.FOUND, track);
        }

        public SearchEventResultStatus getStatus() {
            return status;
        }

        public MusicAudioTrack getTrack() {
            return track;
        }

    }
}
