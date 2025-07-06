package io.lolyay.panel.Packet.Packets.C2S.headless;

import com.google.gson.annotations.Expose;
import io.lolyay.JdaMain;
import io.lolyay.musicbot.HeadlessMode;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.BeaconablePacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.img.S2CFetchImagePacket;
import io.lolyay.panel.Packet.Packets.S2C.utilpackets.S2CSuccessPacket;
import io.lolyay.utils.ImageFetcher;
import io.lolyay.utils.Logger;
import org.java_websocket.WebSocket;

public class C2SStartHeadlessPacket extends BeaconablePacket implements C2SPacket {
    @Expose
    public HeadlessMode.HeadlessData data;

    @Override
    public void recivePacket(WebSocket socket) {
        if(data == null) return;
        Logger.warn("[HEADLESS] Starting headless mode...");
        Logger.log(data.tracks.length + " tracks");
        JdaMain.headlessMode.enable(data);
        PacketHandler.sendPacket(socket,new S2CSuccessPacket(getOpcode(),""));
    }

    @Override
    public int getOpcode() {
        return 901;
    }

    @Override
    public void recivePacket() {
        Logger.warn("[HEADLESS] Starting headless mode...");
        JdaMain.headlessMode.enable(data);
    }
}
