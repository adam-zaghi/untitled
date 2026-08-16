package com.dailycodework.trackingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Parcel {

    private Long id;
    private Long orderId;

    private String status;
    private String priority;

    private LocalDateTime estimatedDelivery;
}