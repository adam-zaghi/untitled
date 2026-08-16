package com.dailycodework.trackingservice.service;

import com.dailycodework.trackingservice.dto.DeliveryDestinationResponse;
import com.dailycodework.trackingservice.dto.UpdatePositionRequest;
import com.dailycodework.trackingservice.entities.DeliveryEstimate;
import com.dailycodework.trackingservice.entities.TrackingEvent;

import java.util.List;

public interface TrackingService {

    TrackingEvent updatePosition(UpdatePositionRequest request);

    List<TrackingEvent> getParcelHistory(Long parcelId);

    TrackingEvent getLastPosition(Long parcelId);

    DeliveryEstimate getParcelEstimate(Long parcelId);
    DeliveryDestinationResponse getParcelDestination(Long parcelId);
}