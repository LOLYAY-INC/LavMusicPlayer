package io.lolyay.music.abstracts;

import io.lolyay.search.Search;
import io.lolyay.music.track.MusicAudioTrack;

import java.util.function.Consumer;

public abstract class AbstractPlayerManager {

    public abstract void searchWithDefaultOrder(String query, Consumer<Search> callback);

    public abstract void playTrack(MusicAudioTrack track);

    public abstract void stop();

    public abstract void pause();

    public abstract void resume();

    public abstract void setVolume(int volume);

}