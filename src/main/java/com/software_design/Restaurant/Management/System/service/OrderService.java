package com.software_design.Restaurant.Management.System.service;

import ch.qos.logback.core.status.Status;
import com.software_design.Restaurant.Management.System.entity.Menu;
import com.software_design.Restaurant.Management.System.entity.Order;
import com.software_design.Restaurant.Management.System.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;

    public boolean isMenuMappedToOrder(int foodId) {
        return repository.existsOrderByMenuId(foodId);
    }

    public Order saveOrder(Order order) {
        order.setStatus(order.getStatus());
        return repository.save(order);
    }

    public List<Order> getOrders() {
        return repository.findAll();
    }

    public Order getOrderById(int orderId) {
        return repository.findById(orderId).orElse(null);
    }

    public Order updateOrderStatus(Order order) {
        Order existingOrder = repository.findById(order.getOrderId()).orElse(null);
        existingOrder.setStatus(order.getStatus());
        return repository.save(existingOrder);
    }
    public List<Order> getOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByDateBetween(startDate, endDate);
    }

    public List<Menu> getTopMenus() {
        return repository.findMostOrderedMenus();
    }
}