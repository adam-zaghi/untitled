package com.dailycodework.orderservice.service;

import com.dailycodework.orderservice.entitites.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    List<Order> getOrdersByCustomerId(Long customerId);

    Order updateOrder(Long id, Order order);

    Order validateOrder(Long id);

    Order cancelOrder(Long id);

    void deleteOrder(Long id);
}