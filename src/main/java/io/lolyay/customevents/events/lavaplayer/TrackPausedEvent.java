package io.lolyay.customevents.events.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.lolyay.customevents.Event;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

public class TrackPausedEvent extends Event {
    private final String guildId;
    private final MusicAudioTrack track;
    private final AudioPlayer player;

    public TrackPausedEvent(String guildId, MusicAudioTrack track, AudioPlayer player) {
        this.guildId = guildId;
        this.track = track;
        this.player = player;
    }

    public String getGuildId() {
        return guildId;
    }

    public MusicAudioTrack getTrack() {
        return track;
    }

    public AudioPlayer getPlayer() {
        return player;
    }
}
