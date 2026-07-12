package com.dailycodework.parcelservice.service;
import com.dailycodework.parcelservice.entities.Parcel;
import com.dailycodework.parcelservice.entities.ParcelStatusHistory;
import com.dailycodework.parcelservice.entities.Status;

import java.util.List;

public interface ParcelService {

    Parcel createParcel(Parcel parcel);

    List<Parcel> getAllParcels();

    Parcel getParcelById(Long id);

    List<Parcel> getParcelsByOrderId(Long orderId);

    List<Parcel> getParcelsByStatus(Status status);

    Parcel updateParcel(Long id, Parcel parcel);

    Parcel updateParcelStatus(Long id, Status newStatus, Long changedBy, String comment);

    List<ParcelStatusHistory> getParcelHistory(Long parcelId);

    void deleteParcel(Long id);
}