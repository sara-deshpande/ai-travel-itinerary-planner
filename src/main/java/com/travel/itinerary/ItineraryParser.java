package com.travel.itinerary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItineraryParser {

    public static Itinerary parse(String json) {
        JSONObject root = new JSONObject(json);
        JSONArray dayArray = root.getJSONArray("days");

        List<Itinerary.Day> days = new ArrayList<>();
        for (int i = 0; i < dayArray.length(); i++) {
            JSONObject d = dayArray.getJSONObject(i);

            List<Itinerary.Restaurant> restaurants = new ArrayList<>();
            JSONArray restaurantArray = d.getJSONArray("restaurants");
            for (int j = 0; j < restaurantArray.length(); j++) {
                JSONObject r = restaurantArray.getJSONObject(j);
                restaurants.add(new Itinerary.Restaurant(
                        r.getString("meal"), r.getString("name"), r.getString("note")));
            }

            days.add(new Itinerary.Day(
                    d.getInt("dayNumber"),
                    d.getString("title"),
                    toList(d.getJSONArray("morning")),
                    toList(d.getJSONArray("afternoon")),
                    toList(d.getJSONArray("evening")),
                    toList(d.getJSONArray("tips")),
                    restaurants));
        }
        return new Itinerary(root.getString("destination"), days);
    }

    private static List<String> toList(JSONArray array) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            result.add(array.getString(i));
        }
        return result;
    }
}