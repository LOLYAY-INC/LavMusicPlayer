package io.lolyay;

import io.lolyay.config.ConfigLoader;
import io.lolyay.config.ConfigManager;
import io.lolyay.panel.Server;
import io.lolyay.utils.Logger;

public class ShutdownHandler {
    public static void shutdown() {
        try {
            Logger.log("Shutting down player...");
            Server.stopserver();
            ConfigManager.saveConfig();
            Logger.success("Shutdown complete");
        } catch (Exception e) {
            Logger.err("Error during shutdown: " + e.getMessage());
        }
    }
}
