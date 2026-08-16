package com.dailycodework.trackingservice.repository;

import com.dailycodework.trackingservice.entities.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {

    List<TrackingEvent> findByParcelIdOrderByEventTimeDesc(Long parcelId);

    Optional<TrackingEvent> findFirstByParcelIdOrderByEventTimeDesc(Long parcelId);
}