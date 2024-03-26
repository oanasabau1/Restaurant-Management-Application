package com.software_design.Restaurant.Management.System.controller;

import com.software_design.Restaurant.Management.System.dto.DatesDTO;
import com.software_design.Restaurant.Management.System.dto.OrderDTO;
import com.software_design.Restaurant.Management.System.entity.Menu;
import com.software_design.Restaurant.Management.System.entity.Order;
import com.software_design.Restaurant.Management.System.mapper.OrderMapper;
import com.software_design.Restaurant.Management.System.service.MenuService;
import com.software_design.Restaurant.Management.System.service.OrderService;
import com.software_design.Restaurant.Management.System.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;
    private final MenuService menuService;
    private final OrderMapper orderMapper;
    private final UserService userService;

    private Double calculateTotalPrice(List<Menu> menus) {
        double totalPrice = 0.0;
        for (Menu m : menus) {
            totalPrice += m.getPrice();
        }
        return totalPrice;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDto) {
        Order order = orderMapper.orderDtoToEntity(orderDto);
        order.setTotalPrice(calculateTotalPrice(orderDto.orderedMenu())); // Changed orderDto.orderedMenu() to orderDto.getOrderedMenu()
        List<Menu> menus = new ArrayList<>();
        for (Menu menu : orderDto.orderedMenu()) {
            Menu tmp = menuService.getMenuById(menu.getMenuId());
            if (tmp != null) {
                if (tmp.getStock() > 0) {
                    tmp.setStock(tmp.getStock() - 1);
                    menus.add(tmp);
                } else {
                    return ResponseEntity.badRequest().body("Food with ID " + menu.getMenuId() + " is out of stock"); // Changed "Food with ID " + menu.getMenuId() + " out of stock" to "Food with ID " + menu.getMenuId() + " is out of stock"
                }
            } else {
                return ResponseEntity.badRequest().body("Food with ID " + menu.getMenuId() + " not found");
            }
        }
        order.setOrderedMenu(menus);
        order.setTotalPrice(this.calculateTotalPrice(menus));
        Order savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(savedOrder);
    }



    @GetMapping("/order/id:{id}")
    public Order findOrderById(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        return order;
    }

    @GetMapping("/allOrders")
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable int id, @RequestParam String status) {
        Order order = orderService.getOrderById(id);
        order.setStatus(status);
        return ResponseEntity.ok(orderService.updateOrderStatus(order));
    }

    @GetMapping("/seeStatistics")
    public List<Order> getOrdersDates(@RequestBody DatesDTO find) {
        if (userService.isAdminLogged()) {
            return orderService.getOrdersBetweenDates(find.startDate(), find.endDate());
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/topMenus")
    public List<Menu> getTopFoods() {
        if (userService.isAdminLogged()) {
            return orderService.getTopMenus();
        } else {
            return Collections.emptyList();
        }
    }
}