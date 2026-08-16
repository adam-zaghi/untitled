package com.dailycodework.trackingservice.service;

import com.dailycodework.trackingservice.dto.routing.RouteRequest;
import com.dailycodework.trackingservice.dto.routing.RouteResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutingServiceImp implements RoutingService {

    private final RestTemplate restTemplate;

    @Value("${openrouteservice.api-key}")
    private String apiKey;

    @Value("${openrouteservice.base-url}")
    private String baseUrl;

    @Override
    public RouteSummary calculateRoute(
            Double startLatitude,
            Double startLongitude,
            Double destinationLatitude,
            Double destinationLongitude
    ) {
        String url = baseUrl + "/v2/directions/driving-car/json";

        RouteRequest request = RouteRequest.builder()
                .coordinates(List.of(
                        List.of(startLongitude, startLatitude),
                        List.of(destinationLongitude, destinationLatitude)
                ))
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", apiKey);

        HttpEntity<RouteRequest> entity = new HttpEntity<>(request, headers);

        RouteResponse response = restTemplate.postForObject(
                url,
                entity,
                RouteResponse.class
        );

        if (response == null ||
                response.getRoutes() == null ||
                response.getRoutes().isEmpty() ||
                response.getRoutes().get(0).getSummary() == null) {
            throw new RuntimeException("Unable to calculate route");
        }

        Double distance = response.getRoutes().get(0).getSummary().getDistance();
        Double duration = response.getRoutes().get(0).getSummary().getDuration();

        return RouteSummary.builder()
                .distanceMeters(distance)
                .durationSeconds(Math.round(duration))
                .build();
    }
}