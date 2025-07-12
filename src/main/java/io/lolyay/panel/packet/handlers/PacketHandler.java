package io.lolyay.panel.packet.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.lolyay.LavMusicPlayer;
import io.lolyay.events.api.packet.PrePacketSendEvent;
import io.lolyay.panel.Server;
import io.lolyay.panel.packet.BeaconablePacket;
import io.lolyay.panel.packet.C2SPacket;
import io.lolyay.panel.packet.S2CPacket;
import io.lolyay.utils.Logger;
import org.java_websocket.WebSocket;


import java.util.Map;

public class PacketHandler {

    public static C2SPacket fromReceivedString(String s) {
        //  Logger.warn(s);

        try {
            Map<String, Object> parsedMap = new Gson().fromJson(s, Map.class);
            if (!parsedMap.containsKey("opcode") || parsedMap.get("opcode") == null) {
                Logger.err("[PANEL] Invalid packet: " + s);
                return null;
            }
            Logger.debug("[PANEL] Got packet with Opcode: " + parsedMap.get("opcode"));
            return PacketRegistry.createPacket(((Double) parsedMap.get("opcode")).intValue(),s);
        } catch (JsonSyntaxException e) {
            Logger.err("[PANEL] Failed to parse packet: " + s);
        }
        return null;
        // UNKNOWN PACKET
    }

    public static BeaconablePacket fromReceivedStringBeaconAble(String s) {
        //  Logger.warn(s);

        try {
            Map<String, Object> parsedMap = new Gson().fromJson(s, Map.class);
            if (!parsedMap.containsKey("opcode") || parsedMap.get("opcode") == null) {
                Logger.err("[PANEL] Invalid packet: " + s);
                return null;
            }
            Logger.debug("[PANEL] Got packet with Opcode: " + parsedMap.get("opcode"));
            C2SPacket packet = PacketRegistry.createPacket(((Double) parsedMap.get("opcode")).intValue(),s);
         /*   if(!packet.getClass().isAssignableFrom(BeaconablePacket.class)) {
                Logger.err("[PANEL] Packet is not beaconable: " + s);
                Logger.err("[PANEL] Packet class: " + packet.getClass().getName());
                return null;
            }*/
            return (BeaconablePacket) packet;
        } catch (JsonSyntaxException e) {
            Logger.err("[PANEL] Failed to parse packet: " + s);
        }
        return null;
        // UNKNOWN PACKET
    }

    public static void sendPacket(WebSocket socket, S2CPacket packet) {
        if(LavMusicPlayer.eventBus.postAndGet(new PrePacketSendEvent(packet)).isCancelled())
            return;
        socket.send(
                packet.getJSON()
        );

    }

    public static void broadcastPacket(S2CPacket packet) {
        for(WebSocket socket : Server.server.getConnections()) {
            sendPacket(socket, packet);
        }
    }
}
