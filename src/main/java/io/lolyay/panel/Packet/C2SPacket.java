package io.lolyay.panel.Packet;

import org.java_websocket.WebSocket;

/**
 * Base interface for all Client-to-Server packets.
 * These are packets sent from the client to the server.
 */
public interface C2SPacket extends Packet {
    /**
     * Called when the server receives this packet.
     */
    void recivePacket(WebSocket socket);
    
    @Override
    default PacketType getType() {
        return PacketType.C2S;
    }
}
