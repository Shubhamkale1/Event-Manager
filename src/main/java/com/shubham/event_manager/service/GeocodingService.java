package com.shubham.event_manager.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
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

        // If no API key configured, skip geocoding
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("Geocoding API key not configured. " +
                    "Add app.geocoding.api-key to properties. " +
                    "Saving venue without coordinates.");
            return Optional.empty();
        }

        try {
            String query = address + ", "
                    + city + ", "
                    + (state != null ? state + ", " : "")
                    + "India";

            String url = UriComponentsBuilder
                    .fromHttpUrl(
                            "https://geocode.maps.co/search")
                    .queryParam("q", query)
                    .queryParam("api_key", apiKey)
                    .toUriString();

            log.info("Calling geocoding API for: {}",
                    query);

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent",
                    "EventManagementApp/1.0");
            headers.setAccept(
                    java.util.List.of(
                            MediaType.APPLICATION_JSON));

            HttpEntity<String> entity =
                    new HttpEntity<>(headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            String.class
                    );

            JsonNode root = objectMapper
                    .readTree(response.getBody());

            if (root.isArray() && root.size() > 0) {
                JsonNode first = root.get(0);
                BigDecimal lat = new BigDecimal(
                        first.get("lat").asText());
                BigDecimal lon = new BigDecimal(
                        first.get("lon").asText());
                log.info(
                        "Geocoded successfully: {} → {}, {}",
                        query, lat, lon);
                return Optional.of(
                        new BigDecimal[]{lat, lon});
            } else {
                log.warn(
                        "No results found for: {}", query);
            }

        } catch (Exception e) {
            log.error(
                    "Geocoding failed for '{}': {}",
                    address, e.getMessage());
        }

        return Optional.empty();
    }
}