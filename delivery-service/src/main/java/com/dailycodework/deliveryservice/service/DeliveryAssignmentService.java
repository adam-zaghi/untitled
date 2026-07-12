package com.dailycodework.deliveryservice.service;

import com.dailycodework.deliveryservice.dto.AssignDeliveryRequest;
import com.dailycodework.deliveryservice.entities.DeliveryAssignment;
import com.dailycodework.deliveryservice.entities.DeliveryStatus;

import java.util.List;

public interface DeliveryAssignmentService {

    DeliveryAssignment assignParcelToAgent(AssignDeliveryRequest request);

    List<DeliveryAssignment> getAllAssignments();

    DeliveryAssignment getAssignmentById(Long id);

    List<DeliveryAssignment> getAssignmentsByAgentId(Long agentId);

    List<DeliveryAssignment> getAssignmentsByParcelId(Long parcelId);

    List<DeliveryAssignment> getAssignmentsByStatus(DeliveryStatus status);

    DeliveryAssignment startDelivery(Long id);

    DeliveryAssignment completeDelivery(Long id);

    DeliveryAssignment failDelivery(Long id, String notes);

    DeliveryAssignment cancelDelivery(Long id);

    void deleteAssignment(Long id);
}