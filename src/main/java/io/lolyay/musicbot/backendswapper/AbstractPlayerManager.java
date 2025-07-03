package io.lolyay.musicbot.backendswapper;

import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractPlayerManager {

    public abstract void searchWithDefaultOrder(String query, Optional<Member> member, Consumer<Search> callback, long guildId);

    public abstract GuildMusicManager getGuildMusicManager(long guildId);

    @Deprecated
    public abstract void searchTrack(String query, Member member, Consumer<MusicAudioTrack> successCallback, Runnable failureCallback, Runnable notFoundCallback);

    public abstract void playTrack(MusicAudioTrack track);

    public abstract void stop(long guildId);

    public abstract void pause(long guildId);

    public abstract void resume(long guildId);

    public abstract void setVolume(long guildId, int volume);

    public abstract void connect(AudioChannel channel);

    public abstract void disconnect(Guild voiceChannel);

}