package com.dailycodework.parcelservice.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

public class Order {
    private Long orderId;
    private Long customerId;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Float totalWeight;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime validatedAt;
    private Long deliveryAddressId;
    private Customer customer;

}
