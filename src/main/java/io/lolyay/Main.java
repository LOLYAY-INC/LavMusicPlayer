package io.lolyay;

import io.lolyay.config.ConfigLoader;
import io.lolyay.utils.KVPair;
import io.lolyay.utils.Logger;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        parseArgs(args);
        Logger.log("Starting bot...");
        try {
            ConfigLoader.load();
        } catch (FileNotFoundException e) {
            Logger.err("Error creating / reading config file.");
            System.exit(1);
        }
        Logger.debug("Loading JDA...");
        try {
            JdaMain.init();
        }catch (Exception e){
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
                JdaMain.debug = true;
                break;
            }

            case "-OVERWRITE_CONFIG":
            {
                Logger.warn("Overwriting Config...");
                ConfigLoader.overWriteConfig = true;
                break;
            }

        }
    }
}
