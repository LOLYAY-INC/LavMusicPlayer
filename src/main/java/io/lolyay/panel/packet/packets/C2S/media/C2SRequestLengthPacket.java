package io.lolyay.panel.packet.packets.C2S.media;

import com.google.gson.annotations.Expose;
import io.lolyay.music.track.MusicAudioTrack;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.C2SPacket;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.packets.S2C.media.S2CRequestLengthPacket;
import org.java_websocket.WebSocket;


public class C2SRequestLengthPacket extends AbstractPacket implements C2SPacket {
    @Expose
    public MusicAudioTrack track;
    @Override
    public void recivePacket(WebSocket socket) {
        long length = MusicAudioTrack.ofEncoded(track.getEncoded()).audioTrack().getDuration();
        PacketHandler.broadcastPacket(new S2CRequestLengthPacket(length,track));

    }

    @Override
    public int getOpcode() {
        return 291;
    }


}
