package com.dailycodework.parcelservice.events;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelCreatedEvent {
    private String eventId;
    private Long parcelId;
    private Long orderId;
    private LocalDateTime occurredAt;
}