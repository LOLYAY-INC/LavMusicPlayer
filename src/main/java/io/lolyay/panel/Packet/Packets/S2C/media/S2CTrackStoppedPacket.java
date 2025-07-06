package io.lolyay.panel.Packet.Packets.S2C.media;

import com.google.gson.annotations.Expose;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.S2CPacket;

public class S2CTrackStoppedPacket extends AbstractPacket implements S2CPacket {
    public S2CTrackStoppedPacket() {
        PacketHandler.broadcastPacket(new S2CUpdatePlayerPacket()); // Update the player, this packet is for fast playlist scheduling
    }

    @Override
    public int getOpcode() {
        return 201;
    }
}
