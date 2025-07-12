package io.lolyay.events.api.packet;

import io.lolyay.eventbus.CancellableEvent;
import io.lolyay.panel.packet.S2CPacket;
import jdk.jfr.Event;

public class PrePacketSendEvent extends CancellableEvent {
    private final S2CPacket packet;
    public PrePacketSendEvent(S2CPacket packet) {
        this.packet = packet;
    }
    public S2CPacket getPacket() {
        return packet;
    }
}
