package io.lolyay.panel.Packet;

/**
 * Base interface for all Server-to-Client packets.
 * These are packets sent from the server to the client.
 */
public interface S2CPacket extends Packet {
    @Override
    default PacketType getType() {
        return PacketType.S2C;
    }
}
