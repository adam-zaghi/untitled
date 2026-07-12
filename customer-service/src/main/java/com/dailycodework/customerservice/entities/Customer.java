package com.dailycodework.customerservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity @Table(name = "customers")
@Data @AllArgsConstructor
@NoArgsConstructor @Builder

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Address> addresses=new ArrayList<>();



}
