package com.dailycodework.trackingservice.repository;

import com.dailycodework.trackingservice.entities.DeliveryEstimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryEstimateRepository extends JpaRepository<DeliveryEstimate, Long> {

    Optional<DeliveryEstimate> findByParcelId(Long parcelId);
}