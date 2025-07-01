package io.lolyay.musicbot;

import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.NodeOptions;
import dev.arbjerg.lavalink.client.event.ClientEvent;
import dev.arbjerg.lavalink.libraries.jda.JDAVoiceUpdateListener;
import io.lolyay.JdaMain;
import io.lolyay.LavaLinkPenaltyProvider;
import io.lolyay.config.ConfigManager;
import io.lolyay.config.jsonnodes.NodesJsonManager;
import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.JDABuilder;

import java.util.ArrayList;
import java.util.Objects;

public class LavaLinkSetup {
    public static LavalinkClient setup(long botId, JDABuilder jdaBuilder) {
        LavalinkClient lavaLinkClient = new LavalinkClient(botId);

        setupLink(lavaLinkClient);

        jdaBuilder.setVoiceDispatchInterceptor(new JDAVoiceUpdateListener(lavaLinkClient));
        Logger.log("Lavalink Setup Complete");
        return lavaLinkClient;
    }



    public static void setupLink(LavalinkClient lavaLinkClient) {
        if(ConfigManager.getConfigBool("using-nodes-json-file")){
            Logger.debug("Loading Nodes from nodes.json file...");
            ArrayList<NodeOptions> nodes = NodesJsonManager.loadNodes();
            for (NodeOptions node : nodes) {
                lavaLinkClient.addNode(node);
            }

        }
        else {
            lavaLinkClient.addNode(
                    new NodeOptions.Builder()
                            .setName("LavaLinkServer")
                            .setPassword(ConfigManager.getConfig("lavalink-password"))
                            .setServerUri(getConProtocol() + ConfigManager.getConfig("lavalink-host") + ":" + ConfigManager.getConfig("lavalink-port"))
                            .build()
            );
            Logger.debug("Loading One Node from Config...");
        }

        lavaLinkClient.getLoadBalancer().addPenaltyProvider(new LavaLinkPenaltyProvider());
        lavaLinkClient.on(ClientEvent.class).subscribe(JdaMain.eventBus::postLavaLink);

    }

    private static String getConProtocol(){
        return Objects.equals(ConfigManager.getConfig("lavalink-secure"), "true") ? "wss://" : "ws://";
    }

}
