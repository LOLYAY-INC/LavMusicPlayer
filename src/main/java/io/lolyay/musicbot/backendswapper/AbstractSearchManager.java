package io.lolyay.musicbot.backendswapper;

import io.lolyay.musicbot.search.Search;
import net.dv8tion.jda.api.entities.Member;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractSearchManager {
    private final long guildId;

    public AbstractSearchManager(long guildId) {
        this.guildId = guildId;
    }

    protected long getGuildId() {
        return guildId;
    }

    public abstract void searchWithDefaultOrder(String query, Optional<Member> member, Consumer<Search> callback, long guildId);


}
