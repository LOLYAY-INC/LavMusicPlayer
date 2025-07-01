package io.lolyay.musicbot.lyrics.records.live;

import io.lolyay.musicbot.lyrics.records.SearchLyrics;

public record LiveLyrics(SearchLyrics query, String content, String source, LiveData liveData) {
}
