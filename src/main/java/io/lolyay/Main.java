package io.lolyay;

import io.lolyay.config.ConfigLoader;
import io.lolyay.utils.Logger;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        Logger.log("Starting bot...");
        try {
            ConfigLoader.load();
        } catch (FileNotFoundException e) {
            Logger.err("Error creating / reading config file.");
            System.exit(1);
        }
    }
}
