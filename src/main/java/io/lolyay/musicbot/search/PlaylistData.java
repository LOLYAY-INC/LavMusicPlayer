package io.lolyay.musicbot.search;

import dev.arbjerg.lavalink.client.player.Track;
import dev.arbjerg.lavalink.protocol.v4.PlaylistInfo;
import io.lolyay.musicbot.RequestorData;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

import java.util.List;

public record PlaylistData(List<MusicAudioTrack> tracks, PlaylistInfo playlistInfo) {
    public static PlaylistData fromTracksAndInfo(List<Track> tracks, PlaylistInfo info, RequestorData userData) {
        return new PlaylistData(tracks.stream().map(track -> MusicAudioTrack.fromTrack(track, userData)).toList(), info);
    }

    public MusicAudioTrack selectedTrack() {
        return tracks.get(playlistInfo.getSelectedTrack());
    }

    public String name() {
        return playlistInfo.getName();
    }
}
