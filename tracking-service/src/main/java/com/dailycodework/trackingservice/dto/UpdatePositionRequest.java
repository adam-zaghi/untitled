package com.dailycodework.trackingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePositionRequest {

    private Long parcelId;
    private Long deliveryAssignmentId;
    private Long deliveryAgentId;

    private Double latitude;
    private Double longitude;
    private Double accuracy;
    private Double speed;

    private String locationLabel;
    private String description;

    private Double remainingDistanceMeters;
    private Long remainingDurationSeconds;
}