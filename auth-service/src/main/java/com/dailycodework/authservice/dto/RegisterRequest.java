package com.dailycodework.authservice.dto;

import com.dailycodework.authservice.entities.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Role role;
}