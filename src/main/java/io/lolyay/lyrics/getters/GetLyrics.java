package io.lolyay.lyrics.getters;

import java.util.ArrayList;
import java.util.List;

public abstract class GetLyrics {
    private static final LyricsGetter[] lyricsGetters = new LyricsGetter[]{
            new MusixMatchGetter()
    };

    public static ArrayList<LyricsGetter> getLyricsGetters() {
        return new ArrayList<>(List.of(lyricsGetters));
    }

    public static LyricsGetterLiveAble getLyricsGetterForLive() {
        return new MusixMatchGetter();
    }

    public static LyricsGetter getLyricsGetterForText() {
        return lyricsGetters[0];
    }
}
