package com.dailycodework.parcelservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "parcelsStatusHistory")
public class ParcelStatusHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Status oldStatus;
    private Status newStatus;
    private String comment;
    private LocalDateTime changedAt;
    private Long changedBy;

    @ManyToOne
    @JsonIgnore
    private Parcel parcel;
}
