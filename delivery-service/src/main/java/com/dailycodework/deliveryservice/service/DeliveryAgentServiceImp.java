package com.dailycodework.deliveryservice.service;

import com.dailycodework.deliveryservice.entities.DeliveryAgentProfile;
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

    @Override
    public DeliveryAgentProfile createAgent(DeliveryAgentProfile agent) {
        agent.setAvailable(agent.getAvailable() != null ? agent.getAvailable() : true);
        agent.setCreatedAt(LocalDateTime.now());
        agent.setUpdatedAt(LocalDateTime.now());

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

        oldAgent.setUserId(newAgent.getUserId());
        oldAgent.setFirstName(newAgent.getFirstName());
        oldAgent.setLastName(newAgent.getLastName());
        oldAgent.setPhone(newAgent.getPhone());
        oldAgent.setCurrentCity(newAgent.getCurrentCity());

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
}