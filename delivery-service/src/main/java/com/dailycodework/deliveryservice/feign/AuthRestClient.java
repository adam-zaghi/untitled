package com.dailycodework.deliveryservice.feign;

import com.dailycodework.deliveryservice.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-service")
public interface AuthRestClient {

    @GetMapping("/api/auth/users/{id}")
    User getUserById(@PathVariable("id") Long id);
    @GetMapping("/api/auth/users/by-email")
    User getUserByEmail(@RequestParam("email") String email);
    @PutMapping("/api/auth/internal/users/{id}/role")
    User updateUserRole(
            @PathVariable("id") Long id,
            @RequestParam("role") String role,
            @RequestHeader("X-Internal-Secret") String secret
    );
}