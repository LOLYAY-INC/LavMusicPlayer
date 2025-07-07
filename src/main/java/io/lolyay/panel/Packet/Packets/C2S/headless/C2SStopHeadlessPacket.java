package io.lolyay.panel.Packet.Packets.C2S.headless;

import io.lolyay.LavMusicPlayer;
import io.lolyay.panel.Packet.BeaconablePacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.utilpackets.S2CSuccessPacket;
import io.lolyay.utils.Logger;
import org.java_websocket.WebSocket;

public class C2SStopHeadlessPacket extends BeaconablePacket implements C2SPacket {

    @Override
    public void recivePacket(WebSocket socket) {
        LavMusicPlayer.headlessMode.disable();
        Logger.warn("[HEADLESS] Stopped headless mode...");
        PacketHandler.sendPacket(socket,new S2CSuccessPacket(getOpcode(),""));
    }

    @Override
    public int getOpcode() {
        return 902;
    }

    @Override
    public void recivePacket() {
        LavMusicPlayer.headlessMode.disable();
        Logger.warn("[HEADLESS] Stopped headless mode...");
    }
}
