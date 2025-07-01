package io.lolyay.musicbot.search;

import io.lolyay.musicbot.tracks.MusicAudioTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Search(Optional<MusicAudioTrack> track, String query, String source, SearchResult result,
                     PlaylistData playlistData) {
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

    public static class SearchResult {
        private final Status status;
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
