package com.dailycodework.parcelservice.kafka;

import com.dailycodework.parcelservice.entities.Status;
import com.dailycodework.parcelservice.events.DeliveryStatusEvent;
import com.dailycodework.parcelservice.service.ParcelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryEventConsumer {

    private final ObjectMapper objectMapper;
    private final ParcelService parcelService;

    @KafkaListener(topics = "delivery.started", groupId = "parcel-service")
    public void consumeDeliveryStarted(String message) {
        DeliveryStatusEvent event = readEvent(message);

        parcelService.updateParcelStatus(
                event.getParcelId(),
                Status.OUT_FOR_DELIVERY,
                event.getAgentUserId(),
                "Delivery started via Kafka"
        );
    }

    @KafkaListener(topics = "delivery.completed", groupId = "parcel-service")
    public void consumeDeliveryCompleted(String message) {
        DeliveryStatusEvent event = readEvent(message);

        parcelService.updateParcelStatus(
                event.getParcelId(),
                Status.DELIVERED,
                event.getAgentUserId(),
                "Delivery completed via Kafka"
        );
    }

    private DeliveryStatusEvent readEvent(String message) {
        try {
            return objectMapper.readValue(message, DeliveryStatusEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while reading DeliveryStatusEvent", e);
        }
    }
}