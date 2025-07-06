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

    Emoji(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


}
