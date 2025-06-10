package io.lolyay.utils;

public enum Emoji {
    WARN("⚠️"),
    SUCCESS("✅"),
    ERROR("❌"),
    LOADING("⌛"),
    MUSIC("🎶"),
    PLAY("▶️"),
    PAUSE("⏸️"),
    SEARCH("🔎");


    private String code;
    private net.dv8tion.jda.api.entities.emoji.Emoji emoji;

    Emoji(String code) {
        this.code = code;
        this.emoji = net.dv8tion.jda.api.entities.emoji.Emoji.fromUnicode(code);
    }

    public String getCode() {
        return code;
    }

    public net.dv8tion.jda.api.entities.emoji.Emoji getEmoji() {
        return emoji;
    }

}
