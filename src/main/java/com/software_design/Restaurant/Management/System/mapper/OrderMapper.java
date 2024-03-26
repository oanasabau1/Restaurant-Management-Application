package com.software_design.Restaurant.Management.System.mapper;

import com.software_design.Restaurant.Management.System.dto.OrderDTO;
import com.software_design.Restaurant.Management.System.entity.Order;
import com.software_design.Restaurant.Management.System.entity.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderMapper {

    public Order orderDtoToEntity(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderedMenu(orderDTO.orderedMenu());
        order.setStatus(orderDTO.status());
        order.setTime(orderDTO.time());

        double totalPrice = orderDTO.orderedMenu().stream()
                .mapToDouble(Menu::getPrice)
                .sum();
        order.setTotalPrice(totalPrice);

        return order;
    }
}