package io.lolyay.musicbot.backendswapper;


import io.lolyay.musicbot.backendswapper.client.ClientInitializer;
import io.lolyay.musicbot.backendswapper.lavaplayer.LavaInitializer;
import io.lolyay.musicbot.backendswapper.structs.ENVIRONMENT;

public class EnviromentFactory {
    public static void create(ENVIRONMENT environment) {
        switch (environment) {
            case CLIENT:
                new ClientInitializer().init();
                break;
            case LAVALINK:
                new LavaInitializer().init();
                break;
        }
    }
}
