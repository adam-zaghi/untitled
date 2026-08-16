package com.dailycodework.deliveryservice.entities;

import com.dailycodework.deliveryservice.dto.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery_agents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryAgentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;


    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    private Boolean available;
    private String currentCity;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DeliveryAssignment> assignments = new ArrayList<>();
}