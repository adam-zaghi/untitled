package com.dailycodework.deliveryservice.service;

import com.dailycodework.deliveryservice.dto.CreateDeliveryAgentRequest;
import com.dailycodework.deliveryservice.entities.DeliveryAgentProfile;

import java.util.List;

public interface DeliveryAgentService {

    DeliveryAgentProfile createAgent(CreateDeliveryAgentRequest request);

    List<DeliveryAgentProfile> getAllAgents();

    DeliveryAgentProfile getAgentById(Long id);

    List<DeliveryAgentProfile> getAvailableAgents();

    DeliveryAgentProfile updateAgent(Long id, DeliveryAgentProfile agent);

    void deleteAgent(Long id);
    Long countAgentsAvailable();
    Long countAgent();

    DeliveryAgentProfile getAgentByUserId(Long userId);
}