package io.lolyay;

import dev.arbjerg.lavalink.client.LavalinkNode;
import dev.arbjerg.lavalink.client.loadbalancing.VoiceRegion;
import dev.arbjerg.lavalink.client.loadbalancing.builtin.IPenaltyProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LavaLinkPenaltyProvider implements IPenaltyProvider {

    @Override
    public int getPenalty(@NotNull LavalinkNode lavalinkNode, @Nullable VoiceRegion voiceRegion) {
        return lavalinkNode.getStats() != null ? lavalinkNode.getStats().getPlayingPlayers() : 1;
    }
}
