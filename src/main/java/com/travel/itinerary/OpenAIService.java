package com.travel.itinerary;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    public String generateItinerary(String destination, int days, String interests) {

        String prompt = "Create a detailed day-by-day travel itinerary for a " + days +
                "-day trip to " + destination + ". The traveler is interested in: " +
                interests + ". Format it clearly with Day 1, Day 2, etc. " +
                "For each day include morning, afternoon, and evening activities.";

        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);

        JSONArray messages = new JSONArray();
        messages.put(message);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 1500);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            // Print raw response to console
            System.out.println("=== RAW API RESPONSE ===");
            System.out.println(response.body());
            System.out.println("========================");

            JSONObject responseJson = new JSONObject(response.body());

            // Check if there's an error in the response
            if (responseJson.has("error")) {
                String errorMsg = responseJson.getJSONObject("error").getString("message");
                return "API Error: " + errorMsg;
            }

            return responseJson
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating itinerary: " + e.getMessage();
        }
    }
}