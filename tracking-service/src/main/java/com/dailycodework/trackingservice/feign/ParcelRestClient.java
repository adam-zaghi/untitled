package com.dailycodework.trackingservice.feign;

import com.dailycodework.trackingservice.dto.Parcel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "parcel-service")
public interface ParcelRestClient {

    @GetMapping("/api/parcels/{id}")
    Parcel getParcelById(@PathVariable("id") Long id);
}