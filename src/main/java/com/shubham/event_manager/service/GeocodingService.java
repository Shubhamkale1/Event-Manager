package com.shubham.event_manager.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeocodingService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.geocoding.api-key:}")
    private String apiKey;

    public Optional<BigDecimal[]> getCoordinates(
            String address, String city, String state) {

        if (apiKey == null || apiKey.isBlank()) {
            log.warn("Geocoding API key not configured.");
            return Optional.empty();
        }

        try {
            // Rate limit protection
            Thread.sleep(1000);

            // Use + encoding exactly like browser URL
            String query = (address + " "
                    + city + " India")
                    .replace(" ", "+");

            // Build URL manually to preserve + encoding
            String url = "https://geocode.maps.co/search"
                    + "?q=" + query
                    + "&api_key=" + apiKey;

            log.info("Geocoding URL: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent",
                    "EventManagementApp/1.0");
            headers.setAccept(
                    List.of(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity =
                    new HttpEntity<>(headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            String.class
                    );

            log.info("Raw response: {}",
                    response.getBody());

            JsonNode root = objectMapper
                    .readTree(response.getBody());

            if (root.isArray() && root.size() > 0) {
                JsonNode first = root.get(0);
                BigDecimal lat = new BigDecimal(
                        first.get("lat").asText());
                BigDecimal lon = new BigDecimal(
                        first.get("lon").asText());
                log.info(
                        "Geocoded successfully → " +
                                "lat={}, lon={}", lat, lon);
                return Optional.of(
                        new BigDecimal[]{lat, lon});
            }

            log.warn("Empty results for: {}", url);

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.error("Geocoding interrupted");
        } catch (Exception e) {
            log.error("Geocoding error: {}",
                    e.getMessage());
        }

        return Optional.empty();
    }
}