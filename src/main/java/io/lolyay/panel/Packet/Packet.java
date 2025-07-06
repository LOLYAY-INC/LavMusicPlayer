package io.lolyay.panel.Packet;

import com.google.gson.GsonBuilder;

public interface Packet {
    /**
     * @return The unique opcode that identifies this packet type
     */
    int getOpcode();
    
    /**
     * @return The type of this packet (C2S for Client-to-Server, S2C for Server-to-Client)
     */
    default PacketType getType() {
        return PacketType.UNKNOWN;
    }

    default String getJSON() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
    
    enum PacketType {
        C2S,  // Client to Server
        S2C,  // Server to Client
        UNKNOWN
    }
}
