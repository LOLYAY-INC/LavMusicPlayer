package io.lolyay.musicbot.tracks;

import dev.arbjerg.lavalink.client.player.Track;
import io.lolyay.musicbot.RequestorData;

import java.sql.Timestamp;
import java.util.Objects;

public class MusicAudioTrack {
    private Track track;
    private RequestorData userData;
    private Timestamp startTime;

    public MusicAudioTrack(Track track, RequestorData userData) {
        this.track = track;
        this.userData = userData;
    }

    public Track track() {
        return track;
    }

    public void track(Track track) {
        this.track = track;
    }

    public Timestamp startTime() {
        return startTime;
    }

    public void startTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public RequestorData userData() {
        return userData;
    }

    public void userData(RequestorData userData) {
        this.userData = userData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicAudioTrack that = (MusicAudioTrack) o;
        return Objects.equals(track, that.track) &&
                Objects.equals(userData, that.userData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(track, userData);
    }

    @Override
    public String toString() {
        return "MusicAudioTrack[" +
                "track=" + track +
                ", userData=" + userData +
                ']';
    }

    public static MusicAudioTrack fromTrack(Track track, RequestorData userData) {
        return new MusicAudioTrack(track, userData);
    }
}