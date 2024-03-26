package com.software_design.Restaurant.Management.System.dto;

import lombok.*;

@Builder
public record UserDTO(
        String name,
        String username,
        Boolean logged,
        String role) {
}