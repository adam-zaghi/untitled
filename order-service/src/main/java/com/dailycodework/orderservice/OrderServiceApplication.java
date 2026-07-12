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

@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(OrderRepository orderRepository, CustomerRestClient customerRestClient) {
        return args -> {

            Customer customer = customerRestClient.getCustomerById(1L);

            CollectionModel<Address> addressesResponse =
                    customerRestClient.getCustomerAddresses(customer.getId());

            Address defaultAddress = addressesResponse.getContent()
                    .stream()
                    .filter(address -> Boolean.TRUE.equals(address.getIsDefault()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Ce client n'a pas d'adresse par défaut"));

            Order order = Order.builder()
                    .status(OrderStatus.DRAFT)
                    .totalWeight(22500F)
                    .customerId(customer.getId())
                    .deliveryAddressId(defaultAddress.getId())
                    .build();

            orderRepository.save(order);

            System.out.println("----------------------");
            System.out.println("Order ID : " + order.getOrderId());
            System.out.println("Ville : " + defaultAddress.getCity());
            System.out.println("Pays : " + defaultAddress.getCountry());
            System.out.println("Status : " + order.getStatus());
            System.out.println("Customer ID : " + order.getCustomerId());
            System.out.println("Delivery Address ID : " + order.getDeliveryAddressId());
        };
    }
};
