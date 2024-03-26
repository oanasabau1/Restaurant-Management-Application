package com.software_design.Restaurant.Management.System.controller;

import com.software_design.Restaurant.Management.System.dto.LoginDTO;
import com.software_design.Restaurant.Management.System.dto.UserDTO;
import com.software_design.Restaurant.Management.System.entity.User;
import com.software_design.Restaurant.Management.System.mapper.UserMapper;
import com.software_design.Restaurant.Management.System.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserMapper userMapper;
    @Autowired
    private UserService service;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO credentials) {
        try {
            String username = credentials.getUsername();
            String password = credentials.getPassword();
            if (service.validateUserCredentials(username, password)) {
                service.setActive(service.getUserByUsername(username));
                return ResponseEntity.ok("User is logged in successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
            }
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String username) {
        try {
            User user = service.getUserByUsername(username);
            if (user != null && user.isLogged()) {
                user.setLogged(false);
                service.saveUser(user);
                return ResponseEntity.ok("User logged out successfully!");
            } else {
                return ResponseEntity.badRequest().body("User is not logged in.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during logout.");
        }
    }


    @PostMapping("/addAdmin")
    public ResponseEntity<UserDTO> addAdmin(@RequestBody User user) {
        try {
            user.setRole("ADMIN");
            User savedUser = service.saveAdmin(user);
            UserDTO userDto = userMapper.userEntityToDto(savedUser);
            return ResponseEntity.ok(userDto);
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            if (service.isAdminLogged()) {
                User savedUser = service.saveUser(user);
                UserDTO userDto = userMapper.userEntityToDto(savedUser);
                return ResponseEntity.ok(userDto);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can add employees.");
            }
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable int id) {
        UserDTO userDto = userMapper.userEntityToDto(service.getUserById(id));
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> allUsers = service.getAllUsers();
        if (!allUsers.isEmpty()) {
            return ResponseEntity.ok(allUsers);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
