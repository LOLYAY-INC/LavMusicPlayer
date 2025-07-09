package io.lolyay.events.api.packet;

import io.lolyay.eventbus.Event;
import io.lolyay.panel.packet.C2SPacket;

public class PacketReceivedEvent extends Event {
    private final C2SPacket packet;

    public PacketReceivedEvent(C2SPacket packet) {
        this.packet = packet;
    }

    public C2SPacket getPacket() {
        return packet;
    }
}
