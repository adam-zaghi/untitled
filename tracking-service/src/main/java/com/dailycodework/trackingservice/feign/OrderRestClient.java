package com.dailycodework.trackingservice.feign;

import com.dailycodework.trackingservice.dto.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service")
public interface OrderRestClient {

    @GetMapping("/api/orders/{orderId}/delivery-address")
    Address getDeliveryAddress(@PathVariable("orderId") Long orderId);
}