package com.dailycodework.trackingservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tracking_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parcelId;

    private Long deliveryAssignmentId;

    private Long deliveryAgentId;

    private Double latitude;

    private Double longitude;

    private Double accuracy;

    private String locationLabel;

    @Enumerated(EnumType.STRING)
    private TrackingEventType eventType;

    private LocalDateTime eventTime;

    private String description;
}