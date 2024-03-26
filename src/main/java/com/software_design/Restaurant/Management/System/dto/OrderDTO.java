package com.software_design.Restaurant.Management.System.dto;

import com.software_design.Restaurant.Management.System.entity.Menu;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderDTO(
        List<Menu> orderedMenu,
        double totalPrice,
        String status,
        LocalDateTime time) {
}
