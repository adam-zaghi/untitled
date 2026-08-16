package com.dailycodework.deliveryservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDeliveryAgentRequest {

    private String userEmail;
    private String currentCity;
    private Boolean available;
}