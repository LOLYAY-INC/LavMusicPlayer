package io.lolyay;

import io.lolyay.config.ConfigLoader;
import io.lolyay.config.ConfigManager;
import io.lolyay.eventbus.EventBus;
import io.lolyay.features.ErrorDetectSystem;
import io.lolyay.features.headless.HeadlessMode;
import io.lolyay.lyrics.getters.LyricsGetterManager;
import io.lolyay.music.lavalink.LavaLinkPlayerManager;
import io.lolyay.music.MusicManager;
import io.lolyay.music.lavalink.LavaInitializer;
import io.lolyay.features.jtmc.MediaManager;
import io.lolyay.music.output.Pcm16Player;
import io.lolyay.music.structs.ENVIRONMENT;
import io.lolyay.panel.Server;
import io.lolyay.panel.webserver.HttpServer;
import io.lolyay.features.tray.TrayManager;
import io.lolyay.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;


public class LavMusicPlayer {
    public static final int PLAYER_UPDATE_INTERVAL = 500;


    public static boolean debug = false;
    public static boolean silent = false;
    public static boolean shouldExtract = true;
    public static boolean exposePort = false;

    public static HeadlessMode headlessMode = new HeadlessMode();
    public static MediaManager mediaManager;
    public static MusicManager musicManager;
    public static Pcm16Player player;
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

        player = new Pcm16Player(playerManager.getPlayerFactory().getOrCreatePlayer());

        mediaManager = new MediaManager(eventBus, "LavMusicPlayer", "lavmusicplayer");

        Logger.log("Music Setup Complete");

        try {
            HttpServer.start();
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

        String url = "http://localhost:" + HttpServer.port;
        try {
            Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (IOException | URISyntaxException ex) {
            JOptionPane.showMessageDialog(null, "Failed to open player", "Error", JOptionPane.ERROR_MESSAGE);
        }

        LyricsGetterManager.initMusixMatch(ConfigManager.getConfig().getLyrics().getMusicmatchAuthToken());
        Logger.debug("Lyrics Setup Complete");

        eventBus.register(new ErrorDetectSystem());
        Logger.debug("ErrorDetectSystem started.");
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
