package io.lolyay.musicbot.tracks;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.arbjerg.lavalink.client.player.Track;
import io.lolyay.musicbot.RequestorData;

import java.util.Objects;

public class MusicAudioTrack {
    private Track track;
    private TrackInfo trackInfo;
    private AudioTrack audioTrack;
    private RequestorData userData;
    private long startTime;

    public MusicAudioTrack(Track track, RequestorData userData) {
        this.track = track;
        this.trackInfo = new TrackInfo(track.getInfo().getTitle(), track.getInfo().getAuthor(), track.getInfo().getArtworkUrl());
        this.userData = userData;
    }

    public MusicAudioTrack(AudioTrack track, RequestorData userData) {
        this.audioTrack = track;
        this.trackInfo = new TrackInfo(track.getInfo().title, track.getInfo().author, track.getInfo().artworkUrl);
        this.userData = userData;
    }

    public static MusicAudioTrack fromTrack(AudioTrack track, RequestorData userData) {
        return new MusicAudioTrack(track, userData);
    }

    public TrackInfo trackInfo() {
        return trackInfo;
    }

    public Track track() {
        return track;
    }

    public void track(Track track) {
        this.track = track;
    }

    public void trackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }

    public AudioTrack audioTrack() {
        return audioTrack;
    }

    public long startTime() {
        return startTime;
    }

    public void startTime(long timestamp) {
        this.startTime = timestamp;
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

    public void audioTrack(AudioTrack audioTrack) {
        this.audioTrack = audioTrack;
    }
}