package io.lolyay.eventbus.events.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import dev.arbjerg.lavalink.client.LavalinkNode;
import io.lolyay.eventbus.Event;
import io.lolyay.music.track.MusicAudioTrack;

public class TrackStartedEvent extends Event {

    private final MusicAudioTrack track;
    private final AudioPlayer player;

    public TrackStartedEvent(MusicAudioTrack track, AudioPlayer player) {
        this.track = track;
        this.player = player;
    }

    public MusicAudioTrack getTrack() {
        return track;
    }


    public AudioPlayer getPlayer() {
        return player;
    }
}
