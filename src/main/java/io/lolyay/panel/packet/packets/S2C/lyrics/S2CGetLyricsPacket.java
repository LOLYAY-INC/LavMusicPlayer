package io.lolyay.panel.packet.packets.S2C.lyrics;

import com.google.gson.annotations.Expose;
import io.lolyay.lyrics.records.Lyrics;
import io.lolyay.music.track.MusicAudioTrack;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.S2CPacket;

public class S2CGetLyricsPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public MusicAudioTrack track;
    @Expose
    public Lyrics lyrics;

    public S2CGetLyricsPacket(MusicAudioTrack track, Lyrics lyrics) {
        this.track = track;
        this.lyrics = lyrics;
    }


    @Override
    public int getOpcode() {
        return 801;
    }
}

