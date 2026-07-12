package com.dailycodework.parcelservice.feign;

import com.dailycodework.parcelservice.dto.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderRestClient {
    @GetMapping("/api/orders/{id}")
    Order getOrderById(@PathVariable Long id);

}
