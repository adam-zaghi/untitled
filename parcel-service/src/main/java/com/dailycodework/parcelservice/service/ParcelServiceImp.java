package com.dailycodework.parcelservice.service;

import com.dailycodework.parcelservice.entities.Parcel;
import com.dailycodework.parcelservice.entities.ParcelStatusHistory;
import com.dailycodework.parcelservice.entities.Priority;
import com.dailycodework.parcelservice.entities.Status;
import com.dailycodework.parcelservice.feign.OrderRestClient;
import com.dailycodework.parcelservice.dto.Order;
import com.dailycodework.parcelservice.repository.ParcelRepository;
import com.dailycodework.parcelservice.repository.ParcelStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParcelServiceImp implements ParcelService {

    private final ParcelRepository parcelRepository;
    private final ParcelStatusHistoryRepository parcelStatusHistoryRepository;
    private final OrderRestClient orderRestClient;

    @Override
    public Parcel createParcel(Parcel parcel) {

        Order order = orderRestClient.getOrderById(parcel.getOrderId());

        if (order == null || order.getOrderId() == null) {
            throw new RuntimeException("Order not found with id: " + parcel.getOrderId());
        }

        if (parcel.getStatus() == null) {
            parcel.setStatus(Status.CREATED);
        }

        if (parcel.getPriority() == null) {
            parcel.setPriority(Priority.NORMAL);
        }

        parcel.setCreatedAt(LocalDateTime.now());
        parcel.setUpdatedAt(LocalDateTime.now());

        ParcelStatusHistory history = ParcelStatusHistory.builder()
                .oldStatus(null)
                .newStatus(parcel.getStatus())
                .comment("Parcel created")
                .changedAt(LocalDateTime.now())
                .changedBy(null)
                .parcel(parcel)
                .build();

        parcel.getParcelStatusHistory().add(history);

        Parcel savedParcel = parcelRepository.save(parcel);
        savedParcel.setOrder(order);

        return savedParcel;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parcel> getAllParcels() {
        List<Parcel> parcels = parcelRepository.findAll();

        parcels.forEach(this::attachOrderToParcel);

        return parcels;
    }

    @Override
    @Transactional(readOnly = true)
    public Parcel getParcelById(Long id) {
        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcel not found with id: " + id));

        attachOrderToParcel(parcel);

        return parcel;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parcel> getParcelsByOrderId(Long orderId) {

        Order order = orderRestClient.getOrderById(orderId);

        if (order == null || order.getOrderId() == null) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }

        List<Parcel> parcels = parcelRepository.findByOrderId(orderId);

        parcels.forEach(parcel -> parcel.setOrder(order));

        return parcels;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parcel> getParcelsByStatus(Status status) {
        List<Parcel> parcels = parcelRepository.findByStatus(status);

        parcels.forEach(this::attachOrderToParcel);

        return parcels;
    }

    @Override
    public Parcel updateParcel(Long id, Parcel newParcel) {

        Parcel oldParcel = parcelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcel not found with id: " + id));

        if (newParcel.getOrderId() != null) {
            Order order = orderRestClient.getOrderById(newParcel.getOrderId());

            if (order == null || order.getOrderId() == null) {
                throw new RuntimeException("Order not found with id: " + newParcel.getOrderId());
            }

            oldParcel.setOrderId(newParcel.getOrderId());
        }

        if (newParcel.getWeight() != null) {
            oldParcel.setWeight(newParcel.getWeight());
        }

        if (newParcel.getHeight() != null) {
            oldParcel.setHeight(newParcel.getHeight());
        }

        if (newParcel.getWidth() != null) {
            oldParcel.setWidth(newParcel.getWidth());
        }

        if (newParcel.getLength() != null) {
            oldParcel.setLength(newParcel.getLength());
        }

        if (newParcel.getFragile() != null) {
            oldParcel.setFragile(newParcel.getFragile());
        }

        if (newParcel.getInsured() != null) {
            oldParcel.setInsured(newParcel.getInsured());
        }

        if (newParcel.getPriority() != null) {
            oldParcel.setPriority(newParcel.getPriority());
        }

        if (newParcel.getEstimatedDelivery() != null) {
            oldParcel.setEstimatedDelivery(newParcel.getEstimatedDelivery());
        }

        oldParcel.setUpdatedAt(LocalDateTime.now());

        Parcel savedParcel = parcelRepository.save(oldParcel);
        attachOrderToParcel(savedParcel);

        return savedParcel;
    }

    @Override
    public Parcel updateParcelStatus(Long id, Status newStatus, Long changedBy, String comment) {

        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcel not found with id: " + id));

        Status oldStatus = parcel.getStatus();

        if (oldStatus == newStatus) {
            return parcel;
        }

        parcel.setStatus(newStatus);
        parcel.setUpdatedAt(LocalDateTime.now());

        ParcelStatusHistory history = ParcelStatusHistory.builder()
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .comment(comment)
                .changedAt(LocalDateTime.now())
                .changedBy(changedBy)
                .parcel(parcel)
                .build();

        parcel.getParcelStatusHistory().add(history);

        Parcel savedParcel = parcelRepository.save(parcel);
        attachOrderToParcel(savedParcel);

        return savedParcel;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcelStatusHistory> getParcelHistory(Long parcelId) {

        Parcel parcel = parcelRepository.findById(parcelId)
                .orElseThrow(() -> new RuntimeException("Parcel not found with id: " + parcelId));

        return parcelStatusHistoryRepository.findByParcelId(parcel.getId());
    }

    @Override
    public void deleteParcel(Long id) {

        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcel not found with id: " + id));

        parcelRepository.delete(parcel);
    }

    private void attachOrderToParcel(Parcel parcel) {
        try {
            Order order = orderRestClient.getOrderById(parcel.getOrderId());
            parcel.setOrder(order);
        } catch (Exception e) {
            parcel.setOrder(null);
        }
    }
}