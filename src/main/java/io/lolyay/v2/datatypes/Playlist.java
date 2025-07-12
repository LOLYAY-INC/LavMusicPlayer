package io.lolyay.v2.datatypes;

import com.google.gson.annotations.Expose;
import io.lolyay.music.track.MusicAudioTrack;

public class Playlist {
    @Expose
    private String name;
    @Expose
    private String id;
    @Expose
    private MusicAudioTrack[] tracks;
    @Expose
    private int currentIndex;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MusicAudioTrack[] getTracks() {
        return tracks;
    }

    public void setTracks(MusicAudioTrack[] tracks) {
        this.tracks = tracks;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

}
