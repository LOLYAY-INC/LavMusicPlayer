package io.lolyay.panel;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class Server {
    public static WebSocketServer server;
    public static int PORT = 3272;
    private final ConnectionHandler connectionHandler = new ConnectionHandler();

    public static void stopserver() throws InterruptedException {
        server.stop();
    }

    public void init() {

        server = new WebSocketServer(new InetSocketAddress("localhost", PORT)) {

            @Override
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                connectionHandler.handleConnect(webSocket, clientHandshake);
            }

            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                connectionHandler.handleClose(webSocket, i, s, b);
                    }

            @Override
            public void onMessage(WebSocket webSocket, String s) {
                connectionHandler.handleMessage(webSocket, s);

            }

            @Override
            public void onError(WebSocket webSocket, Exception e) {
                connectionHandler.handleError(webSocket, e);
            }

            @Override
            public void onStart() {
                connectionHandler.handleStart(server);
            }
        };
        server.setReuseAddr(true);// Fix to over 2hrs of debugging
        server.start(); // I almost forgot to start the server
    }




}
