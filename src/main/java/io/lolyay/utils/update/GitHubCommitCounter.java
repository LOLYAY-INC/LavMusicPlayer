package io.lolyay.utils.update;

import io.lolyay.utils.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to fetch the total number of commits from a GitHub repository.
 */
public class GitHubCommitCounter {
    private static final String GITHUB_API_URL = "https://api.github.com/repos/LOLYAY-INC/LavMusicBot/commits?per_page=1";
    private static final Pattern COMMIT_COUNT_PATTERN = Pattern.compile("\\d+$");

    /**
     * Gets the total number of commits from the LavMusicBot GitHub repository.
     *
     * @return The total number of commits as an integer, or -1 if an error occurs.
     */
    public static int getCommitCount() {
        try {
            URL url = new URI(GITHUB_API_URL).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
                Logger.err("Failed to connect to GitHub API: HTTP error code " + connection.getResponseCode());
                return -1;
            }

            // Get the 'Link' header which contains pagination information
            String linkHeader = connection.getHeaderField("Link");
            if (linkHeader == null) {
                Logger.err("No Link header found in GitHub API response");
                return -1;
            }

            // Parse the Link header to find the 'last' URL
            String lastUrl = null;
            String[] links = linkHeader.split(",");
            for (String link : links) {
                if (link.contains("rel=\"last\"")) {
                    // Extract the URL part
                    int startIndex = link.indexOf('<') + 1;
                    int endIndex = link.indexOf('>');
                    lastUrl = link.substring(startIndex, endIndex);
                    break;
                }
            }

            if (lastUrl == null) {
                Logger.err("Could not find 'last' link in GitHub API response");
                return -1;
            }

            // Extract the page number from the URL using regex
            Matcher matcher = COMMIT_COUNT_PATTERN.matcher(lastUrl);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            } else {
                Logger.err("Could not extract commit count from URL: " + lastUrl);
                return -1;
            }
        } catch (IOException e) {
            Logger.err("Error connecting to GitHub API: " + e.getMessage());
            return -1;
        } catch (NumberFormatException e) {
            Logger.err("Error parsing commit count: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            Logger.err("Unexpected error while fetching commit count: " + e.getMessage());
            return -1;
        }
    }
}