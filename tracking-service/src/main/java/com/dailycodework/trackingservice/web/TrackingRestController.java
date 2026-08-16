package com.dailycodework.trackingservice.web;

import com.dailycodework.trackingservice.dto.DeliveryDestinationResponse;
import com.dailycodework.trackingservice.dto.UpdatePositionRequest;
import com.dailycodework.trackingservice.entities.DeliveryEstimate;
import com.dailycodework.trackingservice.entities.TrackingEvent;
import com.dailycodework.trackingservice.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingRestController {

    private final TrackingService trackingService;

    @GetMapping("/parcels/{parcelId}/destination")
    public ResponseEntity<DeliveryDestinationResponse> getParcelDestination(@PathVariable Long parcelId) {
        return ResponseEntity.ok(trackingService.getParcelDestination(parcelId));
    }
    @PostMapping("/positions")
    public ResponseEntity<TrackingEvent> updatePosition(@RequestBody UpdatePositionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(trackingService.updatePosition(request));
    }

    @GetMapping("/parcels/{parcelId}/history")
    public ResponseEntity<?> getParcelHistory(@PathVariable Long parcelId) {
        return ResponseEntity.ok(trackingService.getParcelHistory(parcelId));
    }

    @GetMapping("/parcels/{parcelId}/last-position")
    public ResponseEntity<TrackingEvent> getLastPosition(@PathVariable Long parcelId) {
        return ResponseEntity.ok(trackingService.getLastPosition(parcelId));
    }

    @GetMapping("/parcels/{parcelId}/estimate")
    public ResponseEntity<DeliveryEstimate> getParcelEstimate(@PathVariable Long parcelId) {
        return ResponseEntity.ok(trackingService.getParcelEstimate(parcelId));
    }
}