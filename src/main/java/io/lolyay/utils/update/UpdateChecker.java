package io.lolyay.utils.update;

import io.lolyay.config.ConfigManager;
import io.lolyay.utils.Logger;

import java.util.Arrays;

public class UpdateChecker {
    public static void checkForUpdates() {
        Integer currentVersion;
        try {
            currentVersion = Integer.parseInt(Arrays.stream(ConfigManager.getConfig("version").split("\\.")).toList().getLast());
            Integer ghCommitCount = GitHubCommitCounter.getCommitCount();
            if (currentVersion < ghCommitCount) {
                Logger.warn("A new version is available, please update to version " + ghCommitCount);
            }
        } catch (Exception e) {
            Logger.err("Error checking for updates: " + e.getMessage());
        }
    }
}
