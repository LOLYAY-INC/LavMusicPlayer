package io.lolyay.musicbot;

public class RequestorData {
    private final long userId;
    private final String userName;
    private final long dcGuildId;

    public RequestorData(long userId, String userName, long dcGuildId) {
        this.userId = userId;
        this.userName = userName;
        this.dcGuildId = dcGuildId;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public long getDcGuildId() {
        return dcGuildId;
    }
    
}
