package io.lolyay;

import io.lolyay.config.ConfigLoader;
import io.lolyay.customevents.EventBus;
import io.lolyay.musicbot.HeadlessMode;
import io.lolyay.musicbot.LavaLinkPlayerManager;
import io.lolyay.musicbot.MusicManager;
import io.lolyay.musicbot.LavaInitializer;
import io.lolyay.musicbot.jtmc.MediaManager;
import io.lolyay.musicbot.output.Player;
import io.lolyay.musicbot.structs.ENVIRONMENT;
import io.lolyay.panel.Server;
import io.lolyay.panel.beacon.HttpBeaconServer;
import io.lolyay.tray.TrayManager;
import io.lolyay.utils.Logger;

import java.io.IOException;


public class LavMusicPlayer {
    public static boolean debug = false;
    public static boolean shouldRegisterCommands = true;

    public static HeadlessMode headlessMode = new HeadlessMode();
    public static MediaManager mediaManager;
    public static MusicManager musicManager;
    public static Player player;
    public static LavaLinkPlayerManager playerManager;
    public static ENVIRONMENT environment;
    public static Scheduler scheduledTasksManager = new Scheduler();
    public static EventBus eventBus = new EventBus();

    public static void init() throws InterruptedException {

        Logger.debug("Initializing...");

        environment = ENVIRONMENT.LAVALINK;
        new LavaInitializer().init();
        Logger.debug("Initialized Lavalink.");

        musicManager = new MusicManager(playerManager);

        player = new Player(playerManager.getPlayerFactory().getOrCreatePlayer());

        mediaManager = new MediaManager(eventBus, "LavMusicPlayer", "lavmusicplayer");

        Logger.log("Music Setup Complete");

        try {
            HttpBeaconServer.start();
            Logger.log("Http Server started on port " + (Server.PORT + 1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Server().init();

        Logger.log("Api Server started on port " + Server.PORT);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TrayManager.setupTray();
                Logger.log("Tray Setup Complete");
            }
        });
    }

    public static void init(String configPath) throws InterruptedException {
        try {
            ConfigLoader.load(configPath);
        } catch (IOException e) {
            Logger.err("Error creating / reading config file.");
            System.exit(1);
        }
    }

}
