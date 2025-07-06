package io.lolyay;

import com.sedmelluq.discord.lavaplayer.tools.io.ByteBufferInputStream;
import com.sedmelluq.discord.lavaplayer.tools.io.MessageInput;
import dev.lavalink.youtube.clients.Music;
import io.lolyay.customevents.EventBus;
import io.lolyay.musicbot.HeadlessMode;
import io.lolyay.musicbot.LavaLinkPlayerManager;
import io.lolyay.musicbot.MusicManager;
import io.lolyay.musicbot.abstracts.AbstractPlayerManager;
import io.lolyay.musicbot.LavaInitializer;
import io.lolyay.musicbot.jtmc.MediaManager;
import io.lolyay.musicbot.output.Player;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.structs.ENVIRONMENT;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.panel.Server;
import io.lolyay.panel.beacon.HttpBeaconServer;
import io.lolyay.utils.Logger;

import java.io.IOException;

import static io.lolyay.musicbot.search.Search.SearchResult.Status.SUCCESS;


public class JdaMain {
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

        Logger.debug("Initlializing...");

        environment = ENVIRONMENT.LAVALINK;
        Logger.debug("Using LavaPlayer Backend...");
        new LavaInitializer().init();

        musicManager = new MusicManager(playerManager);

        player = new Player(playerManager.getPlayerFactory().getOrCreatePlayer());

        mediaManager = new MediaManager(eventBus, "LavMusicPlayer", "lavmusicplayer");

        Logger.log("Music Bot Setup Complete");

        try {
            HttpBeaconServer.start(Server.PORT + 1);
            Logger.log("Beacon Server started on port " + (Server.PORT + 1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Server().init();

        Logger.log("Server started on port " + Server.PORT);
    }

}
