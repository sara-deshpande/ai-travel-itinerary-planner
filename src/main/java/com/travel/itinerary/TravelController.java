package com.travel.itinerary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TravelController {

    private static final Logger log = LoggerFactory.getLogger(TravelController.class);

    private final OpenAIService openAIService;
    private final PlaceChecker placeChecker;

    public TravelController(OpenAIService openAIService, PlaceChecker placeChecker) {
        this.openAIService = openAIService;
        this.placeChecker = placeChecker;
    }

    // Shows the home page with the form
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Handles form submission
    @PostMapping("/generate")
    public String generateItinerary(
            @RequestParam String destination,
            @RequestParam int days,
            @RequestParam(defaultValue = "") String interests,
            Model model) {

        System.out.println("Received request for: " + destination + ", " + days + " days, interests: " + interests);

        Map<String, String> errors = new HashMap<>();
        if (destination == null || destination.trim().length() < 2 || destination.trim().length() > 80) {
            errors.put("destination", "Enter a destination between 2 and 80 characters.");
        } else if (!placeChecker.isRealPlace(destination)) {
            errors.put("destination", "We could not find that place. Try a real city or country.");
        }
        if (days < 1 || days > 14) {
            errors.put("days", "Choose between 1 and 14 days.");
        }
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("destination", destination);
            model.addAttribute("days", days);
            model.addAttribute("interests", interests);
            return "index";
        }
        
        try {
            Itinerary itinerary = openAIService.generateItinerary(destination, days, interests);
            model.addAttribute("itinerary", itinerary);
        } catch (Exception e) {
            log.error("Could not plan the trip", e);
            model.addAttribute("error", "Something went wrong while planning your trip. Please try again.");
        }

        model.addAttribute("destination", destination);
        model.addAttribute("days", days);
        model.addAttribute("interests", interests);

        return "result";
    }
}