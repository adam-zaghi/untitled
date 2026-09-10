package com.dailycodework.orderservice.service;

import com.dailycodework.orderservice.entitites.Address;
import com.dailycodework.orderservice.entitites.Customer;
import com.dailycodework.orderservice.entitites.Order;
import com.dailycodework.orderservice.entitites.OrderStatus;
import com.dailycodework.orderservice.feign.CustomerRestClient;
import com.dailycodework.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRestClient customerRestClient;

    @Override
    public Order createOrder(Order order) {

        Customer customer = customerRestClient.getCustomerById(order.getCustomerId());

        if (customer == null || customer.getId() == null) {
            throw new RuntimeException("Customer not found with id: " + order.getCustomerId());
        }

        if (order.getDeliveryAddressId() == null) {
            Long defaultAddressId = getDefaultAddressId(customer.getId());
            order.setDeliveryAddressId(defaultAddressId);
        }

        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.DRAFT);
        }

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());


        Order savedOrder = orderRepository.save(order);
        savedOrder.setCustomer(customer);

        return savedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        orders.forEach(this::attachCustomerToOrder);

        return orders;
    }
    @Override
    @Transactional(readOnly = true)
    public Address getDeliveryAddress(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (order.getDeliveryAddressId() == null) {
            throw new RuntimeException("No delivery address defined for order id: " + orderId);
        }

        return customerRestClient.getAddressById(order.getDeliveryAddressId());
    }

    @Override
    @Transactional
    public void handleParcelCreated(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setTotalParcels(valueOrZero(order.getTotalParcels()) + 1);
        order.setRemainingParcels(valueOrZero(order.getRemainingParcels()) + 1);

        orderRepository.save(order);
    }
    private int valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }

    @Override
    @Transactional
    public void handleParcelStatusChanged(
            Long orderId,
            String oldStatus,
            String newStatus
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (oldStatus.equals(newStatus)) {
           return;
        }
        if ("OUT_FOR_DELIVERY".equals(newStatus)) {
            handleParcelOutForDelivery(order);
        }

        if ("DELIVERED".equals(newStatus)) {
            handleParcelDelivered(order);
        }

        if ("CANCELLED".equals(newStatus) || "RETURNED".equals(newStatus)) {
            handleParcelCancelledOrReturned(order);
        }

        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
    private void handleParcelOutForDelivery(Order order) {
        if (order.getStatus() != OrderStatus.COMPLETED &&
                order.getStatus() != OrderStatus.CANCELLED) {

            order.setStatus(OrderStatus.IN_DELIVERY);
            order.setLockedForDelivery(true);

            if (order.getLockedForDeliveryAt() == null) {
                order.setLockedForDeliveryAt(LocalDateTime.now());
            }
        }
    }
    private void handleParcelDelivered(Order order) {
        order.setDeliveredParcels(valueOrZero(order.getDeliveredParcels()) + 1);

        int remaining = Math.max(0, valueOrZero(order.getRemainingParcels()) - 1);
        order.setRemainingParcels(remaining);

        if (remaining == 0 && valueOrZero(order.getTotalParcels()) > 0) {
            order.setStatus(OrderStatus.COMPLETED);
        }
    }
    private void handleParcelCancelledOrReturned(Order order) {
        order.setCancelledParcels(valueOrZero(order.getCancelledParcels()) + 1);

        int remaining = Math.max(0, valueOrZero(order.getRemainingParcels()) - 1);
        order.setRemainingParcels(remaining);

        if (remaining == 0 && valueOrZero(order.getTotalParcels()) > 0) {
            order.setStatus(OrderStatus.COMPLETED);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        attachCustomerToOrder(order);

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomerId(Long customerId) {

        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Override
    public Order updateOrder(Long id, Order newOrder) {

        Order oldOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (newOrder.getCustomerId() != null) {
            Customer customer = customerRestClient.getCustomerById(newOrder.getCustomerId());

            if (customer == null || customer.getId() == null) {
                throw new RuntimeException("Customer not found with id: " + newOrder.getCustomerId());
            }

            oldOrder.setCustomerId(newOrder.getCustomerId());
        }

        if (newOrder.getTotalWeight() != null) {
            oldOrder.setTotalWeight(newOrder.getTotalWeight());
        }

        if (newOrder.getDeliveryAddressId() != null) {
            oldOrder.setDeliveryAddressId(newOrder.getDeliveryAddressId());
        }

        if (newOrder.getStatus() != null) {
            oldOrder.setStatus(newOrder.getStatus());
        }

        oldOrder.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(oldOrder);
        attachCustomerToOrder(updatedOrder);

        return updatedOrder;
    }

    @Override
    public Order validateOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new RuntimeException("Only DRAFT orders can be validated");
        }

        order.setStatus(OrderStatus.VALIDATED);
        order.setValidatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        attachCustomerToOrder(savedOrder);

        return savedOrder;
    }

    @Override
    public Order cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Delivered order cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        attachCustomerToOrder(savedOrder);

        return savedOrder;
    }

    @Override
    public void deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        orderRepository.delete(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countOrders() {
        return orderRepository.count();
    }

    private void attachCustomerToOrder(Order order) {
        try {
            Customer customer = customerRestClient.getCustomerById(order.getCustomerId());
            order.setCustomer(customer);
        } catch (Exception e) {
            order.setCustomer(null);
        }
    }

    private Long getDefaultAddressId(Long customerId) {

        List<Address> addresses = customerRestClient.getCustomerAddresses(customerId);

        if (addresses == null || addresses.isEmpty()) {
            throw new RuntimeException("No address found for customer id: " + customerId);
        }

        return addresses.stream()
                .filter(address -> Boolean.TRUE.equals(address.getIsDefault()))
                .findFirst()
                .map(Address::getId)
                .orElseThrow(() -> new RuntimeException(
                        "No default address found for customer id: " + customerId
                ));
    }
}