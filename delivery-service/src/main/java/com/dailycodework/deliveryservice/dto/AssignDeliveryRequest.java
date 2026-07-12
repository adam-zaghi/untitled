package com.dailycodework.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignDeliveryRequest {

    private Long parcelId;
    private Long agentId;
    private String notes;
}