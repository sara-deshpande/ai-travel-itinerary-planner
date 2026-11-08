package com.travel.itinerary;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class PlaceChecker {

    private static final Logger log = LoggerFactory.getLogger(PlaceChecker.class);

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public boolean isRealPlace(String name) {
        String url = "https://nominatim.openstreetmap.org/search?format=jsonv2&limit=5&q="
                + URLEncoder.encode(name.trim(), StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(5))
                .header("User-Agent", "ai-travel-itinerary-planner (student project)")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.warn("Place lookup returned status {}", response.statusCode());
                return true;
            }
            JSONArray results = new JSONArray(response.body());
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String category = result.optString("category");
                String type = result.optString("type");
                if (category.equals("place") || (category.equals("boundary") && type.equals("administrative"))) {
                    return true;
                }
            }
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return true;
        } catch (Exception e) {
            log.warn("Place lookup failed: {}", e.getMessage());
            return true;
        }
    }
}