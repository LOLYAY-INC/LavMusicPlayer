package io.lolyay.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.logging.Level;

public class ImageFetcher {

    // A reusable HttpClient instance is recommended for performance
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();


    /**
     * Fetches an image from a given URL and returns it as a Base64 encoded string.
     * This string can be used directly in an HTML img tag's src attribute.
     *
     * @param imageUrl The full URL of the image to fetch.
     * @return A Base64 string formatted as a data URI (e.g., "data:image/jpeg;base64,..."),
     *         or null if the image could not be fetched.
     */
    public static String fetchImageAsBase64(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }

        try {
            // --- TWEAK: Building the request with more realistic browser headers ---
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(imageUrl))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36")
                    .header("Referer", "https://music.youtube.com/") // Very important header
                    .header("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
                    .header("Accept-Language", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("sec-ch-ua", "\"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"138\", \"Google Chrome\";v=\"138\"")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("sec-ch-ua-platform", "\"Windows\"")
                    .GET()
                    .build();

            // Note: Many 'sec-fetch-*' and ':' pseudo-headers are browser-controlled and cannot/should not be set here.
            // This set of headers is a strong representation of a browser request.

            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // Check for a successful response code (e.g., 200 OK)
            if (response.statusCode() / 100 != 2) {
                Logger.log("Failed to fetch image. Status: {0} for URL: {1}");
                return null;
            }

            byte[] imageBytes;
            try (InputStream inputStream = response.body()) {
                imageBytes = inputStream.readAllBytes();
            }

            String contentType = response.headers()
                    .firstValue("Content-Type")
                    .orElse("image/jpeg"); // Default to jpeg if not found

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // --- TWEAK: Logging a concise success message instead of the huge Base64 string ---
            // Assuming you have a custom Logger class. If not, you can use the standard one:
            // LOGGER.log(Level.INFO, "Successfully fetched image for URL: {0}", imageUrl);
            Logger.log("Successfully fetched image for URL: " + imageUrl); // Your original logger call, but with a better message.

            return "data:" + contentType + ";base64," + base64Image;

        } catch (IOException | InterruptedException e) {
            // --- TWEAK: Logging exceptions with more context ---
            Logger.log("Network or thread error fetching image for URL: ");
            Thread.currentThread().interrupt(); // Restore the interrupted status
            return null;
        } catch (IllegalArgumentException e) {
            Logger.log("Invalid URL provided: ");
            return null;
        }
    }
}