package com.travel.itinerary;

import java.util.List;

public record Itinerary(String destination, List<Day> days) {

    public record Day(
            int dayNumber,
            String title,
            List<String> morning,
            List<String> afternoon,
            List<String> evening,
            List<String> tips,
            List<Restaurant> restaurants) {
    }

    public record Restaurant(String meal, String name, String note) {
    }

    // Temporary: lets the current result page show the itinerary until step 8.
    public String toPlainText() {
        StringBuilder sb = new StringBuilder();
        for (Day d : days) {
            sb.append("Day ").append(d.dayNumber()).append(": ").append(d.title()).append("\n\n");
            appendSection(sb, "Morning", d.morning());
            appendSection(sb, "Afternoon", d.afternoon());
            appendSection(sb, "Evening", d.evening());
            appendSection(sb, "Tips", d.tips());
            sb.append("Restaurants:\n");
            for (Restaurant r : d.restaurants()) {
                sb.append("- ").append(r.meal()).append(": ").append(r.name())
                        .append(" (").append(r.note()).append(")\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static void appendSection(StringBuilder sb, String label, List<String> items) {
        sb.append(label).append(":\n");
        for (String item : items) {
            sb.append("- ").append(item).append("\n");
        }
    }
}