package com.dailycodework.customerservice.feign;

import com.dailycodework.customerservice.entities.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface AuthRestClient {

    @GetMapping("/api/auth/users/{id}")
    User getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/auth/users/by-email")
    User getUserByEmail(@RequestParam("email") String email);
}