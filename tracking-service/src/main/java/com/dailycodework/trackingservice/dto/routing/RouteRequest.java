package com.dailycodework.trackingservice.dto.routing;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteRequest {
    private List<List<Double>> coordinates;
}