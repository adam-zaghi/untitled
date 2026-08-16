package com.dailycodework.orderservice.entitites;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Long id;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private Boolean isDefault;
    private Double latitude;
    private Double longitude;
    private Customer customer;

}
