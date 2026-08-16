package com.dailycodework.customerservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerRequest {
    private String userEmail;
    private AddressRequest defaultAddress;
}