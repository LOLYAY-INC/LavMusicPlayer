package io.lolyay.panel.beacon;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.lolyay.LavMusicPlayer;
import io.lolyay.panel.Packet.BeaconablePacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.utils.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpBeaconServer {
    public static int port = 80;

    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // This context path MUST match the URL used in your Svelte app's fetch call.
        server.createContext("/start-headless-on-close", new BeaconHandler());
        server.createContext("/", new WebServer(""));
        server.createContext("/assets/", new WebServer("/assets"));

        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        new Thread(server::start).start();
    }

    static class BeaconHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // --- CORS Configuration ---
            // These headers are essential for allowing your Svelte app (on localhost:5173)
            // to communicate with this server (on localhost:3273).
            Headers headers = exchange.getResponseHeaders();
            String origin = exchange.getRequestHeaders().getFirst("Origin");
            if (origin != null) {
                headers.add("Access-Control-Allow-Origin", origin);
                headers.add("Access-Control-Allow-Credentials", "true");
            } else {
                headers.add("Access-Control-Allow-Origin", "*");
            }
            headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            headers.add("Access-Control-Allow-Methods", "POST, OPTIONS");
            headers.add("Vary", "Origin");

            // The browser sends a "preflight" OPTIONS request before the actual POST
            // to check if the server allows the request. We must handle it.
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            // --- Handle the actual POST request ---
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            try {
                // Read the request body
                String requestBody;
                try (InputStream is = exchange.getRequestBody()) {
                    requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                }

                // Process the data
                BeaconablePacket packet = PacketHandler.fromReceivedStringBeaconAble(requestBody);
                if (packet == null) {
                    sendResponse(exchange, 400, "Bad Request: Invalid packet");
                    Logger.err("[BEACON] Invalid packet: " + requestBody);
                    return;
                }
                packet.recivePacket();

                // Send a success response
                Logger.log("[BEACON] Beacon received! (could be due to browser closing)");
                sendResponse(exchange, 204, "");

            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }

    static class WebServer implements HttpHandler {
        private String contextPath;

        public WebServer(String contextPath) {
            this.contextPath = contextPath;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().toString();
            if (exchange.getRequestURI().toString().equals("favicon.ico")) {
                return;
            }
            if(path.equals("/")) {
                path = "index.html";
            }
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            String  mimeType = "text/html";
            if (path.endsWith(".js")) {
                mimeType = "text/javascript";
            } else if (path.endsWith(".css")) {
                mimeType = "text/css";
            } else if (path.endsWith(".png")) {
                mimeType = "image/png";
            } else if (path.endsWith(".jpg")) {
                mimeType = "image/jpeg";
            }


            InputStream is = LavMusicPlayer.class.getResourceAsStream("/assets" + path);
            if (is == null) {
                sendResponse(exchange, 404, "Not Found");
                return;
            }
            Logger.log("[HTTP] Serving " + path);
            exchange.getResponseHeaders().add("Content-Type", mimeType);
            exchange.sendResponseHeaders(200, is.available());
            Logger.log("[HTTP] Serving " + mimeType);
            try (OutputStream os = exchange.getResponseBody()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }
}