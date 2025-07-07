package io.lolyay.panel.packet.packets.C2S;

import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.C2SPacket;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.packets.S2C.S2CHelloPacket;
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
