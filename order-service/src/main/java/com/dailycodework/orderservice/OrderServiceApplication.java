package com.dailycodework.orderservice;

import com.dailycodework.orderservice.entitites.Address;
import com.dailycodework.orderservice.entitites.Customer;
import com.dailycodework.orderservice.entitites.Order;
import com.dailycodework.orderservice.entitites.OrderStatus;
import com.dailycodework.orderservice.feign.CustomerRestClient;
import com.dailycodework.orderservice.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.CollectionModel;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }


};
