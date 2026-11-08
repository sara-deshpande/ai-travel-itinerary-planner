package com.travel.itinerary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ItinerarySchema {

    public static JSONObject responseFormat() {
        JSONObject jsonSchema = new JSONObject();
        jsonSchema.put("name", "itinerary");
        jsonSchema.put("strict", true);
        jsonSchema.put("schema", itinerary());

        JSONObject responseFormat = new JSONObject();
        responseFormat.put("type", "json_schema");
        responseFormat.put("json_schema", jsonSchema);
        return responseFormat;
    }

    private static JSONObject itinerary() {
        JSONObject properties = new JSONObject();
        properties.put("destination", string());
        properties.put("days", array(day()));
        return object(properties, List.of("destination", "days"));
    }

    private static JSONObject day() {
        JSONObject properties = new JSONObject();
        properties.put("dayNumber", new JSONObject().put("type", "integer"));
        properties.put("title", string());
        properties.put("morning", array(string()));
        properties.put("afternoon", array(string()));
        properties.put("evening", array(string()));
        properties.put("tips", array(string()));
        properties.put("restaurants", array(restaurant()));
        return object(properties, List.of("dayNumber", "title", "morning", "afternoon",
                "evening", "tips", "restaurants"));
    }

    private static JSONObject restaurant() {
        JSONObject meal = new JSONObject()
                .put("type", "string")
                .put("enum", new JSONArray(List.of("Breakfast", "Lunch", "Dinner")));
        JSONObject properties = new JSONObject();
        properties.put("meal", meal);
        properties.put("name", string());
        properties.put("note", string());
        return object(properties, List.of("meal", "name", "note"));
    }

    private static JSONObject object(JSONObject properties, List<String> required) {
        JSONObject result = new JSONObject();
        result.put("type", "object");
        result.put("properties", properties);
        result.put("required", new JSONArray(required));
        result.put("additionalProperties", false);
        return result;
    }

    private static JSONObject array(JSONObject items) {
        return new JSONObject().put("type", "array").put("items", items);
    }

    private static JSONObject string() {
        return new JSONObject().put("type", "string");
    }
}