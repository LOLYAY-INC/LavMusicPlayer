package io.lolyay;

import io.lolyay.config.ConfigLoader;
import io.lolyay.customevents.events.lifecycle.PreInitEvent;
import io.lolyay.eventlisteners.EventRegistrer;
import io.lolyay.panel.beacon.HttpBeaconServer;
import io.lolyay.utils.KVPair;
import io.lolyay.utils.Logger;

public class Main {
    public static void main(String[] args) {

        parseArgs(args);
        Logger.log("Starting bot...");

        try {
            ConfigLoader.load();
        } catch (Exception e) {
            Logger.err("Error creating / reading config file.");
            System.exit(1);
        }

        Logger.debug("Loading Main...");
        try {
            EventRegistrer.register();

            LavMusicPlayer.eventBus.post(new PreInitEvent());
            LavMusicPlayer.init();
        } catch (Exception e) {
            Logger.err("Error while starting bot: ");
            e.printStackTrace();
        }
    }

    private static void parseArgs(String[] args) {
        for(String arg : args) {
            if (!arg.contains("=")) {
                useArg(new KVPair<>(arg, ""));
                continue;
            }
            String[] dta = arg.split("=");
            KVPair<String, String> pair = new KVPair<>(dta[0], dta[1]);
            useArg(pair);
        }
    }

    private static void useArg(KVPair<String, String> pair) {
        switch (pair.getKey()) {
            case "-DEBUG":
            {
                Logger.log("Debug mode enabled");
                LavMusicPlayer.debug = true;
                break;
            }

            case "-OVERWRITE_CONFIG":
            {
                Logger.warn("Overwriting Config...");
                break;
            }
            case "-NO_REGISTER_COMMANDS":
            {
                Logger.warn("Registering commands...");
                break;
            }
            case "-p", "-port":
                HttpBeaconServer.port = Integer.parseInt(pair.getValue());
                break;
        }
    }

    private static void onShutdown() {
        try {
            Logger.log("Shutting down bot...");
            // Only save if GuildConfigManager is initialized

            Logger.log("Shutdown complete");
        } catch (Exception e) {
            Logger.err("Error during shutdown: " + e.getMessage());
        }
    }
}
