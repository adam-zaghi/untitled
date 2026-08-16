package com.dailycodework.parcelservice.repository;

import com.dailycodework.parcelservice.entities.ParcelStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelStatusHistoryRepository extends JpaRepository<ParcelStatusHistory, Long> {
    List<ParcelStatusHistory> findByParcelId(Long parcelId);
}
