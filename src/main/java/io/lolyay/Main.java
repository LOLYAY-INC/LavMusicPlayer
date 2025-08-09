package io.lolyay;

import io.lolyay.config.ConfigLoader;
import io.lolyay.events.lifecycle.PreInitEvent;
import io.lolyay.eventlisteners.EventRegistrer;
import io.lolyay.panel.webserver.HttpServer;
import io.lolyay.utils.KVPair;
import io.lolyay.utils.Logger;


public class Main {
    public static void main(String[] args) {
        parseArgs(args);
        if(!isLaunchedWithJavaW() && javaWExists() && LavMusicPlayer.silent) {
            Logger.log("Relaunching with java-w...");
            relaunchWithJavaW();
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(ShutdownHandler::shutdown));

        Logger.log("Starting demon...");

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
                ConfigLoader.forceCreateNewConfig();
                break;
            }
            case "-p", "-port":
                HttpServer.port = Integer.parseInt(pair.getValue());
                break;
            case "-s", "-silent":
                Logger.warn("Silent mode enabled");
                LavMusicPlayer.silent = true;
                break;

            case "-noextract", "-no-extract":
                Logger.warn("No extract mode enabled");
                LavMusicPlayer.shouldExtract = false;
                break;
            case "-e", "-expose":
                Logger.warn("Exposing ports to public");
                LavMusicPlayer.exposePort = true;

            default:
                Logger.err("Unknown argument: " + pair.getKey());
        }
    }

    private static boolean javaWExists(){
        try {
            Runtime.getRuntime().exec(new String[]{"java","-version"});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isLaunchedWithJavaW() {
        return System.console() == null;
    }

    private static void relaunchWithJavaW() {
        try {
            Runtime.getRuntime().exec(new String[]{"javaw", "-jar", LavMusicPlayer.class.getProtectionDomain().getCodeSource().getLocation().getPath()});
            System.exit(0);
        } catch (Exception e) {
            Logger.err("Error relaunching with java-w: " + e.getMessage());
        }
    }
}
