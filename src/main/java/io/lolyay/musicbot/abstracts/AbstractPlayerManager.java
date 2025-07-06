package io.lolyay.musicbot.abstracts;

import io.lolyay.musicbot.MusicManager;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractPlayerManager {

    public abstract void searchWithDefaultOrder(String query, Consumer<Search> callback);

    public abstract void playTrack(MusicAudioTrack track);

    public abstract void stop();

    public abstract void pause();

    public abstract void resume();

    public abstract void setVolume(int volume);

}