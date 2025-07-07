package io.lolyay.panel.packet.packets.S2C.media;

import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.S2CPacket;

public class S2CTrackStoppedPacket extends AbstractPacket implements S2CPacket {
    public S2CTrackStoppedPacket() {
        PacketHandler.broadcastPacket(new S2CUpdatePlayerPacket()); // Update the player, this packet is for fast playlist scheduling
    }

    @Override
    public int getOpcode() {
        return 201;
    }
}
