package io.lolyay.panel;

import io.lolyay.LavMusicPlayer;
import io.lolyay.events.api.packet.PacketReceivedEvent;
import io.lolyay.features.YoutubeOauth2Handler;
import io.lolyay.panel.packet.C2SPacket;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.handlers.PacketInitializer;
import io.lolyay.panel.packet.packets.S2C.youtube.S2COauthRequiredPacket;
import io.lolyay.utils.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.util.concurrent.TimeUnit;


public class ConnectionHandler {
    private WebSocketServer server;

    public void handleConnect(WebSocket socket, ClientHandshake clientHandshake) {
        if(YoutubeOauth2Handler.loginRequired){
            PacketHandler.broadcastPacket(new S2COauthRequiredPacket(YoutubeOauth2Handler.deviceCode));
        }
    }

    public void handleClose(WebSocket socket, int i, String s, boolean b) {
    }

    public void handleMessage(WebSocket socket, String s) {
        C2SPacket packet = PacketHandler.fromReceivedString(s);
        if (packet == null) {
            Logger.err("[PANEL] Couldn't parse packet: " + s + " (probably unknown opcode)");
            return;
        }
        packet.recivePacket(socket);
        LavMusicPlayer.eventBus.post(new PacketReceivedEvent(packet));
    }

    public void handleError(WebSocket socket, Exception e) {
    }

    public void handleStart(WebSocketServer serveri) {
        server = serveri;
        PacketInitializer.initialize();
        LavMusicPlayer.scheduledTasksManager.startScheduledTask(new ScheduledPanelUpdater(), LavMusicPlayer.PLAYER_UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }
}
