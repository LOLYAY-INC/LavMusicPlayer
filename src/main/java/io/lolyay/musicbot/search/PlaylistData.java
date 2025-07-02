package io.lolyay.musicbot.search;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.arbjerg.lavalink.client.player.Track;
import dev.arbjerg.lavalink.protocol.v4.PlaylistInfo;
import io.lolyay.musicbot.RequestorData;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

import java.util.List;

public record PlaylistData(List<MusicAudioTrack> tracks, String playlistName, int selectedTrackId) {
    public static PlaylistData fromTracksAndInfo(List<Track> tracks, PlaylistInfo info, RequestorData userData) {
        return new PlaylistData(tracks.stream().map(track -> MusicAudioTrack.fromTrack(track, userData)).toList(), info.getName(), info.getSelectedTrack());
    }

    public static PlaylistData fromTracksAndInfo(List<AudioTrack> tracks, AudioPlaylist info, RequestorData userData) {
        return new PlaylistData(tracks.stream().map(track -> MusicAudioTrack.fromTrack(track, userData)).toList(), info.getName(), tracks.indexOf(info.getSelectedTrack()));
    }

    public MusicAudioTrack selectedTrack() {
        return tracks.get(selectedTrackId());
    }

    public String name() {
        return playlistName();
    }
}
