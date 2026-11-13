package com.travel.itinerary;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItineraryParserTest {

    private static final String VALID_JSON = """
            {
              "destination": "Lisbon",
              "days": [
                {
                  "dayNumber": 1,
                  "title": "Old town and river",
                  "morning": ["Walk through Alfama.", "See the Se Cathedral."],
                  "afternoon": ["Ride Tram 28.", "Visit Castelo de Sao Jorge."],
                  "evening": ["Watch the sunset at Miradouro da Graca.", "Dinner in Bairro Alto."],
                  "tips": ["Wear comfortable shoes.", "Buy a transit day pass."],
                  "restaurants": [
                    {"meal": "Breakfast", "name": "Pastelaria Alfama", "note": "Try the custard tarts."}
                  ]
                }
              ]
            }
            """;

    @Test
    void parsesValidJson() {
        Itinerary itinerary = ItineraryParser.parse(VALID_JSON);

        assertEquals("Lisbon", itinerary.destination());
        assertEquals(1, itinerary.days().size());
        assertEquals("Old town and river", itinerary.days().get(0).title());
        assertEquals(2, itinerary.days().get(0).morning().size());
        assertEquals("Pastelaria Alfama", itinerary.days().get(0).restaurants().get(0).name());
    }

    @Test
    void rejectsTextThatIsNotJson() {
        assertThrows(JSONException.class, () -> ItineraryParser.parse("Here is your trip!"));
    }
}