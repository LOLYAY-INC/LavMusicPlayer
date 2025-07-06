package io.lolyay.panel.Packet.Packets.C2S.media;

import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CUpdatePlayerPacket;
import org.java_websocket.WebSocket;

public class C2SRequestPlayerUpdatePacket extends AbstractPacket implements C2SPacket {
    @Override
    public void recivePacket(WebSocket socket) {
        PacketHandler.sendPacket(socket,new S2CUpdatePlayerPacket());
    }

    @Override
    public int getOpcode() {
        return 101;
    }
}
