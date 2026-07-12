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

@SpringBootApplication
@EnableFeignClients
public class ParcelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParcelServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner init(ParcelRepository parcelRepository, OrderRestClient orderRestClient) {
        return args -> {



            Parcel parcel = Parcel.builder()
                    .height(130D)
                    .weight(1000D)
                    .width(100D)
                    .length(100D)
                    .orderId(1L)
                    .fragile(true)
                    .status(Status.PREPARING)
                    .priority(Priority.NORMAL)
                    .build();

            parcelRepository.save(parcel);
            Order order = orderRestClient.getOrderById(parcel.getOrderId());

            System.out.println("----------------------");
            System.out.println("Order ID : " + order.getOrderId());
            System.out.println("height : " + parcel.getHeight());
            System.out.println("weight : " + parcel.getWeight());
            System.out.println("Status : " + parcel.getStatus());
            System.out.println("Customer ID : " + order.getCustomerId());

        };
    }
}
