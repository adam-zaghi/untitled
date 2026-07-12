package com.dailycodework.customerservice;

import com.dailycodework.customerservice.entities.Address;
import com.dailycodework.customerservice.entities.Customer;
import com.dailycodework.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner init(CustomerRepository customerRepository) {
        return args -> {

            Customer customer = Customer.builder()
                    .firstName("Adam")
                    .lastName("Zaghi")
                    .email("adam@email.com")
                    .phone("0600000000")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .addresses(new ArrayList<>())
                    .build();

            Address address = Address.builder()
                    .street("Lieutenant Mahroud")
                    .city("Casablanca")
                    .country("Morocco")
                    .postalCode("20000")
                    .isDefault(true)
                    .customer(customer)
                    .build();

            customer.getAddresses().add(address);

            customerRepository.save(customer);

            customerRepository.findAll().forEach(c -> {
                System.out.println(c.getFirstName());
                System.out.println(c.getEmail());

                c.getAddresses().forEach(a -> {
                    System.out.println(a.getCity());
                    System.out.println(a.getStreet());
                });
            });
        };
    }
}
