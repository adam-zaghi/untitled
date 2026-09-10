package com.dailycodework.parcelservice.events;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelStatusChangedEvent {
    private String eventId;
    private Long parcelId;
    private Long orderId;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime occurredAt;
}