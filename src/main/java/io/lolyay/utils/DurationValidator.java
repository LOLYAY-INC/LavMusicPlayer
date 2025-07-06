package io.lolyay.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DurationValidator {
    /**
     * A utility class for validating and parsing time duration strings.
     * <p>
     * Supported formats:
     * <ul>
     *     <li>Seconds: {@code 1s}</li>
     *     <li>Minutes: {@code 1m}</li>
     *     <li>Hours: {@code 1h}</li>
     *     <li>Days: {@code 1d}</li>
     * </ul>
     * <p>
     * @return
     * Parsed time in Seconds
     */
    @Nullable
    public static Integer validateDuration(@NotNull String duration) {
        if (duration.contains("s")) {
            return Integer.parseInt(duration.replace("s", ""));
        } else if (duration.contains("m")) {
            return Integer.parseInt(duration.replace("m", "")) * 60;
        } else if (duration.contains("h")) {
            return Integer.parseInt(duration.replace("h", "")) * 60 * 60;
        } else if (duration.contains("d")) {
            return Integer.parseInt(duration.replace("d", "")) * 60 * 60 * 24;
        } else if (duration.contains("w")) {
            return Integer.parseInt(duration.replace("w", "")) * 60 * 60 * 24 * 7;
        } else {
            return null;
        }
    }

}
