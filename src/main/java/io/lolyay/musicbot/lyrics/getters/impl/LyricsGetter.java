package io.lolyay.musicbot.lyrics.getters.impl;


import io.lolyay.musicbot.lyrics.records.Lyrics;

import java.util.concurrent.CompletableFuture;

public abstract class LyricsGetter {
    public abstract String getSourceName();

    public abstract boolean canGetLyrics(String songName);

    public abstract CompletableFuture<Lyrics> getLyrics(String songName);

    public boolean canBeLive() {
        return false;
    }
}
