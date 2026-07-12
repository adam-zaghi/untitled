package com.dailycodework.parcelservice.entities;

import com.dailycodework.parcelservice.dto.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "parcels")
public class Parcel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private Double weight;
    private Double height;
    private Double width;
    private Double length;
    private Boolean fragile;
    private Boolean insured;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDateTime estimatedDelivery;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "parcel")
    List<ParcelStatusHistory> parcelStatusHistory=new ArrayList<>();
    @Transient
    private Order order;

}
