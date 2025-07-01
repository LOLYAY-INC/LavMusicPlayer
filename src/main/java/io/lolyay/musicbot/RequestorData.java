package io.lolyay.musicbot;

import io.lolyay.JdaMain;
import net.dv8tion.jda.api.entities.Member;

import java.util.Optional;

public record RequestorData(long userId, String userName, long dcGuildId) {
    public static RequestorData fromMember(Optional<Member> member, long guildId) {
        final RequestorData userData;
        userData = member.map(value ->
                new RequestorData(value.getIdLong(),
                        value.getEffectiveName(),
                        value.getGuild().getIdLong())
        ).orElseGet(() ->
                new RequestorData(JdaMain.jda.getSelfUser().getIdLong(),
                        "System",
                        guildId)
        );
        return userData;
    }
}
