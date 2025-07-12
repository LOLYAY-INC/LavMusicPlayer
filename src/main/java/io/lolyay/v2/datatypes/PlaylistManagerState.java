package io.lolyay.v2.datatypes;

import com.google.gson.annotations.Expose;
import io.lolyay.music.track.MusicAudioTrack;

public class PlaylistManagerState {
    @Expose
    public Playlist currentPlaylist;
    @Expose
    public RepeatMode repeatMode = RepeatMode.OFF;
    @Expose
    public int currentTrackIndex = 0;
    @Expose
    public MusicAudioTrack currentTrack;

}
