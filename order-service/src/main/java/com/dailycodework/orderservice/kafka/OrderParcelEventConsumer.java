package com.dailycodework.orderservice.kafka;

import com.dailycodework.orderservice.events.ParcelCreatedEvent;
import com.dailycodework.orderservice.events.ParcelStatusChangedEvent;
import com.dailycodework.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderParcelEventConsumer {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @KafkaListener(topics = "parcel.created", groupId = "order-service")
    public void consumeParcelCreated(String message) {
        try {
            log.info("Received parcel.created event: {}", message);

            ParcelCreatedEvent event =
                    objectMapper.readValue(message, ParcelCreatedEvent.class);

            log.info("Updating counters for orderId={}", event.getOrderId());

            orderService.handleParcelCreated(event.getOrderId());

            log.info("Order counters updated for orderId={}", event.getOrderId());

        } catch (Exception e) {
            log.error("Error while consuming parcel.created event", e);
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "parcel.status.changed", groupId = "order-service")
    public void consumeParcelStatusChanged(String message) {
        try {
            log.info("Received parcel.status.changed event: {}", message);

            ParcelStatusChangedEvent event =
                    objectMapper.readValue(message, ParcelStatusChangedEvent.class);

            orderService.handleParcelStatusChanged(
                    event.getOrderId(),
                    event.getOldStatus(),
                    event.getNewStatus()
            );

            log.info("Order status updated for orderId={}", event.getOrderId());

        } catch (Exception e) {
            log.error("Error while consuming parcel.status.changed event", e);
            throw new RuntimeException(e);
        }
    }
}