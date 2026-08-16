package com.dailycodework.trackingservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_estimates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryEstimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parcelId;

    private Double remainingDistanceMeters;
    private Long remainingDurationSeconds;

    private LocalDateTime estimatedArrival;

    private Integer delayInMinutes;
    private Boolean delayed;

    private LocalDateTime updatedAt;
}