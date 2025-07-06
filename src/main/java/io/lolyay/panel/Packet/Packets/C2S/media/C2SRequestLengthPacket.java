package io.lolyay.panel.Packet.Packets.C2S.media;

import com.google.gson.annotations.Expose;
import dev.lavalink.youtube.clients.Music;
import io.lolyay.JdaMain;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CRequestLengthPacket;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CUpdatePlayerPacket;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CUpdatePlayerPositionPacket;
import io.lolyay.panel.Packet.Packets.S2C.utilpackets.S2CSuccessPacket;
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
