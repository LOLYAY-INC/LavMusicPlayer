package io.lolyay.musicbot.lyrics.getters.impl;


import io.lolyay.musicbot.lyrics.records.live.LiveLyrics;

import java.util.concurrent.CompletableFuture;

public abstract class LyricsGetterLiveAble extends LyricsGetter {
    @Override
    public boolean canBeLive() {
        return true;
    }

    public abstract CompletableFuture<LiveLyrics> getLiveLyrics(String songName);

}
