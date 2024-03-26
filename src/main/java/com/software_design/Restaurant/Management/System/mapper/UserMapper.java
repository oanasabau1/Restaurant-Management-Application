package com.software_design.Restaurant.Management.System.mapper;

import com.software_design.Restaurant.Management.System.dto.UserDTO;
import com.software_design.Restaurant.Management.System.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserMapper {

    public UserDTO userEntityToDto(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole())
                .logged(user.isLogged())
                .build();
    }

    public List<UserDTO> userListEntityToDto(List<User> users) {
        return users.stream()
                .map(user -> userEntityToDto(user))
                .toList();
    }

}