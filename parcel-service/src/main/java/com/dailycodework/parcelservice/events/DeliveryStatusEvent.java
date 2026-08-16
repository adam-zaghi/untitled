package com.dailycodework.parcelservice.events;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryStatusEvent {

    private Long deliveryAssignmentId;
    private Long parcelId;

    private Long agentId;
    private Long agentUserId;

    private String eventType;

    private LocalDateTime occurredAt;

    private String comment;
}