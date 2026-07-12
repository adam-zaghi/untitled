package com.dailycodework.deliveryservice.web;

import com.dailycodework.deliveryservice.dto.AssignDeliveryRequest;
import com.dailycodework.deliveryservice.entities.DeliveryAssignment;
import com.dailycodework.deliveryservice.entities.DeliveryStatus;
import com.dailycodework.deliveryservice.service.DeliveryAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryAssignmentRestController {

    private final DeliveryAssignmentService deliveryAssignmentService;

    @PostMapping("/assign")
    public ResponseEntity<DeliveryAssignment> assignParcelToAgent(
            @RequestBody AssignDeliveryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deliveryAssignmentService.assignParcelToAgent(request));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryAssignment>> getAllAssignments() {
        return ResponseEntity.ok(deliveryAssignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryAssignment> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryAssignmentService.getAssignmentById(id));
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<DeliveryAssignment>> getAssignmentsByAgentId(@PathVariable Long agentId) {
        return ResponseEntity.ok(deliveryAssignmentService.getAssignmentsByAgentId(agentId));
    }

    @GetMapping("/parcel/{parcelId}")
    public ResponseEntity<List<DeliveryAssignment>> getAssignmentsByParcelId(@PathVariable Long parcelId) {
        return ResponseEntity.ok(deliveryAssignmentService.getAssignmentsByParcelId(parcelId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeliveryAssignment>> getAssignmentsByStatus(@PathVariable DeliveryStatus status) {
        return ResponseEntity.ok(deliveryAssignmentService.getAssignmentsByStatus(status));
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<DeliveryAssignment> startDelivery(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryAssignmentService.startDelivery(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<DeliveryAssignment> completeDelivery(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryAssignmentService.completeDelivery(id));
    }

    @PatchMapping("/{id}/fail")
    public ResponseEntity<DeliveryAssignment> failDelivery(
            @PathVariable Long id,
            @RequestParam(required = false) String notes
    ) {
        return ResponseEntity.ok(deliveryAssignmentService.failDelivery(id, notes));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<DeliveryAssignment> cancelDelivery(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryAssignmentService.cancelDelivery(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        deliveryAssignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}