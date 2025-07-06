package io.lolyay.musicbot.search;

import com.google.gson.annotations.Expose;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

import java.util.List;
import java.util.Objects;

public final class PlaylistData {
    private final List<MusicAudioTrack> tracks;
    @Expose
    private final String playlistName;
    @Expose
    private final int selectedTrackId;

    public PlaylistData(List<MusicAudioTrack> tracks, String playlistName, int selectedTrackId) {
        this.tracks = List.copyOf(tracks); // Defensive copy for immutability
        this.playlistName = playlistName;
        this.selectedTrackId = selectedTrackId;
    }

    public static PlaylistData fromTracksAndInfo(List<AudioTrack> tracks, AudioPlaylist info) {
        return new PlaylistData(
            tracks.stream()
                .map(MusicAudioTrack::new)
                .toList(), 
            info.getName(), 
            tracks.indexOf(info.getSelectedTrack())
        );
    }

    public MusicAudioTrack selectedTrack() {
        return tracks.get(selectedTrackId);
    }

    public String name() {
        return playlistName;
    }

    public List<MusicAudioTrack> tracks() {
        return List.copyOf(tracks); // Return defensive copy
    }

    public String playlistName() {
        return playlistName;
    }

    public int selectedTrackId() {
        return selectedTrackId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlaylistData that = (PlaylistData) obj;
        return selectedTrackId == that.selectedTrackId &&
               Objects.equals(tracks, that.tracks) &&
               Objects.equals(playlistName, that.playlistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tracks, playlistName, selectedTrackId);
    }

    @Override
    public String toString() {
        return "PlaylistData[" +
               "tracks=" + tracks +
               ", playlistName='" + playlistName + '\'' +
               ", selectedTrackId=" + selectedTrackId +
               ']';
    }
}
