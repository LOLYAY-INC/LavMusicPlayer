package io.lolyay.panel;

import io.lolyay.LavMusicPlayer;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.PacketInitializer;
import io.lolyay.utils.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.util.concurrent.TimeUnit;


public class ConnectionHandler {
    private WebSocketServer server;

    public void handleConnect(WebSocket socket, ClientHandshake clientHandshake) {
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
    }

    public void handleError(WebSocket socket, Exception e) {
    }

    public void handleStart(WebSocketServer serveri) {
        server = serveri;
        PacketInitializer.initialize();
        LavMusicPlayer.scheduledTasksManager.startScheduledTask(new ScheduledPanelUpdater(), 1000, TimeUnit.MILLISECONDS);
    }
}
