package io.lolyay.musicbot.tracks;

import dev.arbjerg.lavalink.client.player.Track;
import io.lolyay.musicbot.RequestorData;

public record MusicAudioTrack(Track track, RequestorData userData) {

}
