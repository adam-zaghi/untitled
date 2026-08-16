package com.dailycodework.deliveryservice.repository;

import com.dailycodework.deliveryservice.entities.DeliveryAgentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryAgentProfileRepository extends JpaRepository<DeliveryAgentProfile, Long> {
    List<DeliveryAgentProfile> findByAvailableTrue();

    List<DeliveryAgentProfile> findByCurrentCity(String currentCity);
    Long countByAvailableTrue();
    boolean existsByUserId(Long userId);

    boolean existsByEmail(String email);
    DeliveryAgentProfile findByUserId(Long userId);

}
