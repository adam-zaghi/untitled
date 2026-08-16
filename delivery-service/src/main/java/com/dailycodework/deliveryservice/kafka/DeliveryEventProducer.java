package com.dailycodework.deliveryservice.kafka;

import com.dailycodework.deliveryservice.events.DeliveryStatusEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishDeliveryStarted(DeliveryStatusEvent event) {
        send("delivery.started", event.getParcelId().toString(), event);
    }

    public void publishDeliveryCompleted(DeliveryStatusEvent event) {
        send("delivery.completed", event.getParcelId().toString(), event);
    }

    private void send(String topic, String key, DeliveryStatusEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while converting event to JSON", e);
        }
    }
}