package com.dailycodework.parcelservice.repository;

import com.dailycodework.parcelservice.entities.Parcel;
import com.dailycodework.parcelservice.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    List<Parcel> findByOrderId(Long orderId);

    List<Parcel> findByStatus(Status status);

    Long countByOrderId(Long orderId);
}