package com.dailycodework.deliveryservice.web;

import com.dailycodework.deliveryservice.dto.CreateDeliveryAgentRequest;
import com.dailycodework.deliveryservice.entities.DeliveryAgentProfile;
import com.dailycodework.deliveryservice.service.DeliveryAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-agents")
@RequiredArgsConstructor
public class DeliveryAgentRestController {

    private final DeliveryAgentService deliveryAgentService;

    @PostMapping
    public ResponseEntity<DeliveryAgentProfile> createAgent(@RequestBody CreateDeliveryAgentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deliveryAgentService.createAgent(request));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryAgentProfile>> getAllAgents() {
        return ResponseEntity.ok(deliveryAgentService.getAllAgents());
    }

    @GetMapping("/count")
    public Long getCount() {
        return deliveryAgentService.countAgent();
    }

    @GetMapping("/count/available")
    public Long getCountAvailable() {
        return deliveryAgentService.countAgentsAvailable();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryAgentProfile> getAgentById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryAgentService.getAgentById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<DeliveryAgentProfile>> getAvailableAgents() {
        return ResponseEntity.ok(deliveryAgentService.getAvailableAgents());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryAgentProfile> updateAgent(
            @PathVariable Long id,
            @RequestBody DeliveryAgentProfile agent
    ) {
        return ResponseEntity.ok(deliveryAgentService.updateAgent(id, agent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        deliveryAgentService.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<DeliveryAgentProfile> getAgentByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(deliveryAgentService.getAgentByUserId(userId));
    }
}