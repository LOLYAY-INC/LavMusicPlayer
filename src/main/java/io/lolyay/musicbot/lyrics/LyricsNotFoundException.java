package io.lolyay.musicbot.lyrics;

import io.lolyay.musicbot.lyrics.records.SearchLyrics;

public class LyricsNotFoundException extends RuntimeException {
    public LyricsNotFoundException(SearchLyrics message,
                                   String source) {
        super(String.format("Lyrics for %s by %s not found in %s", message.title(), message.author(), source));
    }
}
