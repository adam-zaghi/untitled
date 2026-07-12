package com.dailycodework.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parcel {

    private Long id;
    private Long orderId;

    private Double weight;
    private Double height;
    private Double width;
    private Double length;

    private Boolean fragile;
    private Boolean insured;

    private String status;
    private String priority;

    private LocalDateTime estimatedDelivery;
}