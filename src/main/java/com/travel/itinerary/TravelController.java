package com.travel.itinerary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TravelController {

    @Autowired
    private OpenAIService openAIService;

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
            @RequestParam String interests,
            Model model) {

        System.out.println("Received request for: " + destination + ", " + days + " days, interests: " + interests);

        String itinerary = openAIService.generateItinerary(destination, days, interests);

        // Pass data to the result page
        model.addAttribute("destination", destination);
        model.addAttribute("days", days);
        model.addAttribute("itinerary", itinerary);

        return "result";
    }
}