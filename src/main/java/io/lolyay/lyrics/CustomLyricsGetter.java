package io.lolyay.lyrics;

import com.jagrosh.jlyrics.Lyrics;
import com.jagrosh.jlyrics.LyricsClient;
import io.lolyay.utils.Logger;

import java.util.concurrent.CompletableFuture;

public class CustomLyricsGetter {
    private static final LyricsClient client = new LyricsClient();

    public static CompletableFuture<Lyrics> getLyrics(String songName) {
        CompletableFuture<Lyrics> future = new CompletableFuture<>();
        getMusixMatchLyrics(songName).thenAcceptAsync(MLyrics -> {
            if (MLyrics != null) {
                future.complete(MLyrics);
                return;
            }
            Logger.debug("Lyrics not found in MusixMatch, trying Genius...");
            getGeniusLyrics(songName).thenAcceptAsync(gLyrics -> {
                if (gLyrics != null) {
                    future.complete(gLyrics);
                    return;
                }
                Logger.debug("Lyrics not found!");
                future.complete(null);
            });

        });
        return future;
    }

    private static CompletableFuture<Lyrics> getMusixMatchLyrics(String songName) {
        return client.getLyrics(songName, "MusixMatch");
    }

    private static CompletableFuture<Lyrics> getGeniusLyrics(String songName) {
        return client.getLyrics(songName, "Genius");
    }
}
