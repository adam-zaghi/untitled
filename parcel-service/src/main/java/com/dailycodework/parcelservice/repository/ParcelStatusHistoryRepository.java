package com.dailycodework.parcelservice.repository;

import com.dailycodework.parcelservice.entities.ParcelStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ParcelStatusHistoryRepository extends JpaRepository<ParcelStatusHistory, Long> {
    List<ParcelStatusHistory> findByParcelId(Long parcelId);
}
