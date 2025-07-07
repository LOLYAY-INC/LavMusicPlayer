package io.lolyay.panel.packet.packets.S2C.media;

import com.google.gson.annotations.Expose;
import io.lolyay.music.track.MusicAudioTrack;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.S2CPacket;

public class S2CRequestLengthPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public long duration;
    @Expose
    public MusicAudioTrack track;

    public S2CRequestLengthPacket(long length, MusicAudioTrack track) {
        this.duration = length;
        this.track = track;

    }

    @Override
    public int getOpcode() {
        return 291;
    }
}
