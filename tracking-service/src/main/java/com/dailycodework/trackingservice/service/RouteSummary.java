package com.dailycodework.trackingservice.service;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteSummary {
    private Double distanceMeters;
    private Long durationSeconds;
}