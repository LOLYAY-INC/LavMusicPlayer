package io.lolyay.panel.packet.packets.C2S.headless;

import com.google.gson.annotations.Expose;
import io.lolyay.LavMusicPlayer;
import io.lolyay.features.headless.HeadlessMode;
import io.lolyay.panel.packet.BeaconablePacket;
import io.lolyay.panel.packet.C2SPacket;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.packets.S2C.utilpackets.S2CSuccessPacket;
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
        Logger.log(data.playlistId);
        LavMusicPlayer.headlessMode.enable(data);
        PacketHandler.sendPacket(socket,new S2CSuccessPacket(getOpcode(),""));
    }

    @Override
    public int getOpcode() {
        return 901;
    }

    @Override
    public void receivePacket() {
        Logger.warn("[HEADLESS] Starting headless mode...");
        LavMusicPlayer.headlessMode.enable(data);
    }
}
