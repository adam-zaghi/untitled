package com.dailycodework.trackingservice.service;

import com.dailycodework.trackingservice.dto.Address;
import com.dailycodework.trackingservice.dto.DeliveryDestinationResponse;
import com.dailycodework.trackingservice.dto.Parcel;
import com.dailycodework.trackingservice.dto.UpdatePositionRequest;
import com.dailycodework.trackingservice.entities.DeliveryEstimate;
import com.dailycodework.trackingservice.entities.TrackingEvent;
import com.dailycodework.trackingservice.entities.TrackingEventType;
import com.dailycodework.trackingservice.feign.OrderRestClient;
import com.dailycodework.trackingservice.feign.ParcelRestClient;

import com.dailycodework.trackingservice.repository.DeliveryEstimateRepository;
import com.dailycodework.trackingservice.repository.TrackingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TrackingServiceImp implements TrackingService {
    private final OrderRestClient orderRestClient;
    private final TrackingEventRepository trackingEventRepository;
    private final DeliveryEstimateRepository deliveryEstimateRepository;
    private final ParcelRestClient parcelRestClient;
    private final RoutingService routingService;


    @Override
    @Transactional(readOnly = true)
    public DeliveryDestinationResponse getParcelDestination(Long parcelId) {

        Parcel parcel = parcelRestClient.getParcelById(parcelId);

        if (parcel == null || parcel.getId() == null) {
            throw new RuntimeException("Parcel not found with id: " + parcelId);
        }

        if (parcel.getOrderId() == null) {
            throw new RuntimeException("Parcel has no orderId");
        }

        Address address = orderRestClient.getDeliveryAddress(parcel.getOrderId());

        if (address == null || address.getId() == null) {
            throw new RuntimeException("Delivery address not found for parcel id: " + parcelId);
        }

        return DeliveryDestinationResponse.builder()
                .addressId(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .fullAddress(buildFullAddress(address))
                .build();
    }

    private String buildFullAddress(Address address) {
        return String.join(", ",
                address.getStreet() != null ? address.getStreet() : "",
                address.getCity() != null ? address.getCity() : "",
                address.getCountry() != null ? address.getCountry() : ""
        ).replaceAll("(^,\\s*)|(,\\s*$)", "");
    }

    @Override
    public TrackingEvent updatePosition(UpdatePositionRequest request) {

        Parcel parcel = parcelRestClient.getParcelById(request.getParcelId());

        if (parcel == null || parcel.getId() == null) {
            throw new RuntimeException("Parcel not found with id: " + request.getParcelId());
        }

        TrackingEvent trackingEvent = TrackingEvent.builder()
                .parcelId(request.getParcelId())
                .deliveryAssignmentId(request.getDeliveryAssignmentId())
                .deliveryAgentId(request.getDeliveryAgentId())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .accuracy(request.getAccuracy())

                .locationLabel(request.getLocationLabel())
                .eventType(TrackingEventType.POSITION_UPDATED)
                .eventTime(LocalDateTime.now())
                .description(request.getDescription())
                .build();

        TrackingEvent savedEvent = trackingEventRepository.save(trackingEvent);
        try {
            updateDeliveryEstimate(request,parcel);

        } catch (Exception e) {
            System.err.println("Routing calculation failed: " + e.getMessage());
        }

        return savedEvent;
    }
    private void updateDeliveryEstimate(UpdatePositionRequest request,Parcel parcel) {
        DeliveryDestinationResponse destination =
                getParcelDestination(request.getParcelId());

        RouteSummary routeSummary = routingService.calculateRoute(
                request.getLatitude(),
                request.getLongitude(),
                destination.getLatitude(),
                destination.getLongitude()
        );

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime estimatedArrival =
                now.plusSeconds(routeSummary.getDurationSeconds());


        LocalDateTime estimatedDelivery = parcel.getEstimatedDelivery();

        boolean delayed = false;
        int delayInMinutes = 0;

        if (estimatedDelivery != null && estimatedArrival.isAfter(estimatedDelivery)) {
            delayed = true;
            delayInMinutes = (int) Duration.between(
                    estimatedDelivery,
                    estimatedArrival
            ).toMinutes();
        }

        DeliveryEstimate estimate = deliveryEstimateRepository
                .findByParcelId(request.getParcelId())
                .orElse(new DeliveryEstimate());

        estimate.setParcelId(request.getParcelId());
        estimate.setRemainingDistanceMeters(routeSummary.getDistanceMeters());
        estimate.setRemainingDurationSeconds(routeSummary.getDurationSeconds());
        estimate.setEstimatedArrival(estimatedArrival);
        estimate.setDelayed(delayed);
        estimate.setDelayInMinutes(delayInMinutes);
        estimate.setUpdatedAt(now);

        deliveryEstimateRepository.save(estimate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackingEvent> getParcelHistory(Long parcelId) {

        parcelRestClient.getParcelById(parcelId);

        return trackingEventRepository.findByParcelIdOrderByEventTimeDesc(parcelId);
    }

    @Override
    @Transactional(readOnly = true)
    public TrackingEvent getLastPosition(Long parcelId) {

        parcelRestClient.getParcelById(parcelId);

        return trackingEventRepository.findFirstByParcelIdOrderByEventTimeDesc(parcelId)
                .orElseThrow(() -> new RuntimeException("No tracking position found for parcel id: " + parcelId));
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryEstimate getParcelEstimate(Long parcelId) {

        Parcel parcel = parcelRestClient.getParcelById(parcelId);

        if (parcel == null || parcel.getId() == null) {
            throw new RuntimeException("Parcel not found with id: " + parcelId);
        }

        return deliveryEstimateRepository.findByParcelId(parcelId)
                .orElse(
                        DeliveryEstimate.builder()
                                .parcelId(parcel.getId())
                                .remainingDistanceMeters(null)
                                .remainingDurationSeconds(null)
                                .estimatedArrival(null)
                                .delayInMinutes(0)
                                .delayed(false)
                                .updatedAt(null)
                                .build()
                );
    }

}