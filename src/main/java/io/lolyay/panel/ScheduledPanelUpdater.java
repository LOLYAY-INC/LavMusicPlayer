package io.lolyay.panel;

import io.lolyay.JdaMain;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.headless.S2CWeAreHeadlessPacket;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CUpdatePlayerPacket;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CUpdatePlayerPositionPacket;
import io.lolyay.utils.Logger;

public class ScheduledPanelUpdater implements Runnable {
    @Override
    public void run() {
        try {
            PacketHandler.broadcastPacket(new S2CUpdatePlayerPacket());
            PacketHandler.broadcastPacket(new S2CUpdatePlayerPositionPacket());
            if(JdaMain.headlessMode.isHeadless) PacketHandler.broadcastPacket(new S2CWeAreHeadlessPacket());
        } catch (Exception e) {
            Logger.err(e.getMessage());
        }
    }
}
