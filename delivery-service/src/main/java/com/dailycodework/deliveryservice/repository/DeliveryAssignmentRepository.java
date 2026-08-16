package com.dailycodework.deliveryservice.repository;

import com.dailycodework.deliveryservice.entities.DeliveryAssignment;
import com.dailycodework.deliveryservice.entities.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {

    List<DeliveryAssignment> findByAgentId(Long agentId);

    List<DeliveryAssignment> findByParcelId(Long parcelId);

    List<DeliveryAssignment> findByStatus(DeliveryStatus status);

    Long countDeliveryAssignmentByStatus(DeliveryStatus status);

    Long countByStatus(DeliveryStatus status);
}