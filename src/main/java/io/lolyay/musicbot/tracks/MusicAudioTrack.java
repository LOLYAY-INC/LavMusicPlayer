package io.lolyay.musicbot.tracks;

import dev.arbjerg.lavalink.client.player.Track;
import io.lolyay.infusiadc.MusicBot.Rewrite.RequestorData;

public class MusicAudioTrack {
    private Track track;
    private RequestorData userData;

    public MusicAudioTrack(Track track, RequestorData userData) {
        this.track = track;
        this.userData = userData;
    }

    public Track getTrack() {
        return track;
    }

    public RequestorData getUserData() {
        return userData;
    }

}
