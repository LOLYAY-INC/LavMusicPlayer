package io.lolyay.lyrics.getters;

import io.lolyay.lyrics.getters.impl.LyricsGetter;
import io.lolyay.lyrics.getters.impl.LyricsGetterLiveAble;

import java.util.ArrayList;
import java.util.List;

public abstract class LyricsGetterManager {
    private static final LyricsGetter[] lyricsGetters = new LyricsGetter[]{
            new MusixMatchGetter()
    };

    public static ArrayList<LyricsGetter> getLyricsGetters() {
        return new ArrayList<>(List.of(lyricsGetters));
    }

    public static LyricsGetterLiveAble getLyricsGetterForLive() {
        for (LyricsGetter lgetter : lyricsGetters) {
            if (lgetter.canBeLive()) return (LyricsGetterLiveAble) lgetter;
        }
        return null;
    }

    public static LyricsGetter getLyricsGetterForText(String songName) {
        for (LyricsGetter lgetter : lyricsGetters) {
            if (lgetter.canGetLyrics(songName))
                return lgetter;
        }
        return null;
    }
}
