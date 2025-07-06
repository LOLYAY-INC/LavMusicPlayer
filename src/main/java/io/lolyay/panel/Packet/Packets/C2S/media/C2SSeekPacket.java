package io.lolyay.panel.Packet.Packets.C2S.media;

import com.google.gson.annotations.Expose;
import io.lolyay.JdaMain;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CUpdatePlayerPacket;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CUpdatePlayerPositionPacket;
import io.lolyay.panel.Packet.Packets.S2C.utilpackets.S2CSuccessPacket;
import org.java_websocket.WebSocket;
import org.jitsi.impl.neomedia.portaudio.Pa;


public class C2SSeekPacket extends AbstractPacket implements C2SPacket {
    @Expose
    public long seek;
    @Override
    public void recivePacket(WebSocket socket) {


        JdaMain.musicManager.getCurrentTrack().audioTrack().setPosition(seek);

        PacketHandler.sendPacket(socket, new S2CSuccessPacket(getOpcode(), ""));

        PacketHandler.broadcastPacket(new S2CUpdatePlayerPacket());
        PacketHandler.broadcastPacket(new S2CUpdatePlayerPositionPacket());

    }

    @Override
    public int getOpcode() {
        return 118;
    }


}
