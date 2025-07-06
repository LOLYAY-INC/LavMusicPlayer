package io.lolyay.panel.Packet.Packets.C2S;

import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.S2CHelloPacket;
import org.java_websocket.WebSocket;

public class C2SHelloPacket extends AbstractPacket implements C2SPacket {

    @Override
    public void recivePacket(WebSocket socket) {
        PacketHandler.sendPacket(socket,new S2CHelloPacket());

    }

    @Override
    public int getOpcode() {
        return 1;
    }
}
