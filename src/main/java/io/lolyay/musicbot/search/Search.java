package io.lolyay.musicbot.search;

import com.google.gson.annotations.Expose;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Search {
    @Expose
    private final MusicAudioTrack track;
    @Expose
    private final String query;
    @Expose
    private final String source;
    @Expose
    private final SearchResult result;
    @Expose
    private final PlaylistData playlistData;

    public Search(Optional<MusicAudioTrack> track, String query, String source, SearchResult result,
                 PlaylistData playlistData) {
        this.track = track.orElse(null);
        this.query = query;
        this.source = source;
        this.result = result;
        this.playlistData = playlistData;
    }

    public static Search wasError(SearchResult result, String source, String query) {
        return new Search(Optional.empty(), query, source, result, null);
    }

    public static Search wasPlaylist(SearchResult result, String source, String query, PlaylistData playlistData) {
        return new Search(Optional.of(playlistData.selectedTrack()), query, source, result, playlistData);
    }

    public static Search wasTrack(SearchResult result, String source, String query, MusicAudioTrack track) {
        return new Search(Optional.of(track), query, source, result, null);
    }

    public static Search wasNotFound(SearchResult result, String source, String query) {
        return new Search(Optional.empty(), query, source, result, null);
    }

    public Optional<MusicAudioTrack> track() {
        return Optional.ofNullable(track);
    }

    public String query() {
        return query;
    }

    public String source() {
        return source;
    }

    public SearchResult result() {
        return result;
    }

    public PlaylistData playlistData() {
        return playlistData;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Search search = (Search) obj;
        return Objects.equals(track, search.track) &&
               Objects.equals(query, search.query) &&
               Objects.equals(source, search.source) &&
               Objects.equals(result, search.result) &&
               Objects.equals(playlistData, search.playlistData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(track, query, source, result, playlistData);
    }

    @Override
    public String toString() {
        return "Search[" +
               "track=" + track +
               ", query='" + query + '\'' +
               ", source='" + source + '\'' +
               ", result=" + result +
               ", playlistData=" + playlistData +
               ']';
    }

    public static class SearchResult {
        @Expose
        private final Status status;
        @Expose
        private final String message;
        private final List<MusicAudioTrack> playlist = new ArrayList<>();

        private SearchResult(Status status, String message) {
            this.status = status;
            this.message = message;
        }

        public static SearchResult ERROR(String message) {
            return new SearchResult(Status.ERROR, message);
        }

        public static SearchResult SUCCESS(String message) {
            return new SearchResult(Status.SUCCESS, message);
        }

        public static SearchResult NOT_FOUND(String message) {
            return new SearchResult(Status.NOT_FOUND, message);
        }

        public static SearchResult PLAYLIST(String message) {
            return new SearchResult(Status.PLAYLIST, message);
        }

        public static SearchResult ERROR() {
            return ERROR("An unknown error occurred.");
        }

        public static SearchResult SUCCESS() {
            return SUCCESS("Track found successfully.");
        }

        public static SearchResult PLAYLIST() {
            return SUCCESS("Playlist found successfully.");
        }

        public static SearchResult NOT_FOUND() {
            return NOT_FOUND("Could not find a matching track.");
        }

        public Status getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public boolean isSuccess() {
            return status == Status.SUCCESS || status == Status.PLAYLIST;
        }

        public enum Status {
            ERROR,
            SUCCESS,
            NOT_FOUND,
            PLAYLIST
        }
    }
}
