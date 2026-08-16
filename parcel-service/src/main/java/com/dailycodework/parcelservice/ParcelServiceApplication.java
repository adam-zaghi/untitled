package com.dailycodework.parcelservice;

import com.dailycodework.parcelservice.dto.Order;
import com.dailycodework.parcelservice.entities.Parcel;
import com.dailycodework.parcelservice.entities.Priority;
import com.dailycodework.parcelservice.entities.Status;
import com.dailycodework.parcelservice.feign.OrderRestClient;
import com.dailycodework.parcelservice.repository.ParcelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableFeignClients
@EnableKafka
public class ParcelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParcelServiceApplication.class, args);
    }
}
