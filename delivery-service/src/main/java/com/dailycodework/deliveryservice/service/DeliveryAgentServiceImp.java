package com.dailycodework.deliveryservice.service;

import com.dailycodework.deliveryservice.dto.CreateDeliveryAgentRequest;
import com.dailycodework.deliveryservice.dto.User;
import com.dailycodework.deliveryservice.entities.DeliveryAgentProfile;
import com.dailycodework.deliveryservice.feign.AuthRestClient;
import com.dailycodework.deliveryservice.repository.DeliveryAgentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryAgentServiceImp implements DeliveryAgentService {

    private final DeliveryAgentProfileRepository deliveryAgentRepository;
    private final AuthRestClient authRestClient;

    @Override

    public DeliveryAgentProfile createAgent(CreateDeliveryAgentRequest request) {

        if (request.getUserEmail() == null || request.getUserEmail().isBlank()) {
            throw new RuntimeException("User email is required");
        }

        User user = authRestClient.getUserByEmail(request.getUserEmail());

        if (user == null || user.getId() == null) {
            throw new RuntimeException("User not found with email: " + request.getUserEmail());
        }

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException("User account is disabled");
        }

        if (deliveryAgentRepository.existsByUserId(user.getId())) {
            throw new RuntimeException("Delivery agent profile already exists for this user");
        }

        if ("CUSTOMER".equals(user.getRole())) {
            throw new RuntimeException("This user is already a customer");
        }

        if ("ADMIN".equals(user.getRole()) || "LOGISTICS_MANAGER".equals(user.getRole())) {
            throw new RuntimeException("Admin or manager users cannot be converted to delivery agents");
        }

        if ("USER".equals(user.getRole())) {
            user = authRestClient.updateUserRole(
                    user.getId(),
                    "DELIVERY_AGENT",
                    "smartlog-internal-secret"
            );
        }

        if (!"DELIVERY_AGENT".equals(user.getRole())) {
            throw new RuntimeException("User could not be converted to DELIVERY_AGENT");
        }

        DeliveryAgentProfile agent = DeliveryAgentProfile.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .currentCity(request.getCurrentCity())
                .available(request.getAvailable() != null ? request.getAvailable() : true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return deliveryAgentRepository.save(agent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryAgentProfile> getAllAgents() {
        return deliveryAgentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryAgentProfile getAgentById(Long id) {
        return deliveryAgentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery agent not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryAgentProfile> getAvailableAgents() {
        return deliveryAgentRepository.findByAvailableTrue();
    }

    @Override
    public DeliveryAgentProfile updateAgent(Long id, DeliveryAgentProfile newAgent) {

        DeliveryAgentProfile oldAgent = getAgentById(id);

        // Les infos venant du User restent source officielle.
        // Donc on ne modifie pas firstName, lastName, email, phone ici.

        if (newAgent.getCurrentCity() != null) {
            oldAgent.setCurrentCity(newAgent.getCurrentCity());
        }

        if (newAgent.getAvailable() != null) {
            oldAgent.setAvailable(newAgent.getAvailable());
        }

        oldAgent.setUpdatedAt(LocalDateTime.now());

        return deliveryAgentRepository.save(oldAgent);
    }

    @Override
    public void deleteAgent(Long id) {
        DeliveryAgentProfile agent = getAgentById(id);
        deliveryAgentRepository.delete(agent);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAgentsAvailable() {
        return deliveryAgentRepository.countByAvailableTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAgent() {
        return deliveryAgentRepository.count();
    }
    @Override
    @Transactional(readOnly = true)
    public DeliveryAgentProfile getAgentByUserId(Long userId) {
        try {
            DeliveryAgentProfile currentAgent = deliveryAgentRepository.findByUserId(userId);
            return currentAgent;
        }catch (Exception e) {
           throw new RuntimeException("Delivery agent not found for userId: " + userId);
        }
    }
}