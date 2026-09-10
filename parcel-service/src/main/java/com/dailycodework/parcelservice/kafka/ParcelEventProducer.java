package com.dailycodework.parcelservice.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.dailycodework.parcelservice.entities.Parcel;
import com.dailycodework.parcelservice.entities.Status;
import com.dailycodework.parcelservice.events.ParcelCreatedEvent;
import com.dailycodework.parcelservice.events.ParcelStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParcelEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String PARCEL_CREATED_TOPIC = "parcel.created";
    private static final String PARCEL_STATUS_CHANGED_TOPIC = "parcel.status.changed";

    public void sendParcelCreated(Parcel parcel) {
        try {
            ParcelCreatedEvent event = ParcelCreatedEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .parcelId(parcel.getId())
                    .orderId(parcel.getOrderId())
                    .occurredAt(LocalDateTime.now())
                    .build();

            String message = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    PARCEL_CREATED_TOPIC,
                    String.valueOf(parcel.getOrderId()),
                    message
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to send parcel.created event", e);
        }
    }

    public void sendParcelStatusChanged(
            Parcel parcel,
            Status oldStatus,
            Status newStatus
    ) {
        try {
            ParcelStatusChangedEvent event = ParcelStatusChangedEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .parcelId(parcel.getId())
                    .orderId(parcel.getOrderId())
                    .oldStatus(oldStatus.name())
                    .newStatus(newStatus.name())
                    .occurredAt(LocalDateTime.now())
                    .build();

            String message = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    PARCEL_STATUS_CHANGED_TOPIC,
                    String.valueOf(parcel.getOrderId()),
                    message
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to send parcel.status.changed event", e);
        }
    }
}

