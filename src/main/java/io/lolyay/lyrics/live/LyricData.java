package io.lolyay.lyrics.live;

import java.util.List;

public class LyricData {

    // Represents the "time" object: {"total": 8.83, ...}
    public record LyricTime(double total) {
    }

    // Represents a single line: {"text": "...", "time": {...}}
    // The text can be null for instrumental parts, so we use String (not primitive).
    public record LyricLine(String text, LyricTime time) {
    }

    // Represents a section like "verse" or "chorus"
    public record LyricSection(String title, List<LyricLine> lines) {
    }
}