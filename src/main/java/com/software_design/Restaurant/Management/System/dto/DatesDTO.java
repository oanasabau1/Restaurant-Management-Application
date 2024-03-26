package com.software_design.Restaurant.Management.System.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DatesDTO(
        LocalDateTime startDate,
        LocalDateTime endDate) {

}