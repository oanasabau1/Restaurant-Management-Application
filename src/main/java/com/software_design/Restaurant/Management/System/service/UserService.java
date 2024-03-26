package com.software_design.Restaurant.Management.System.service;

import com.software_design.Restaurant.Management.System.utils.Security;
import com.software_design.Restaurant.Management.System.dto.UserDTO;
import com.software_design.Restaurant.Management.System.entity.User;
import com.software_design.Restaurant.Management.System.mapper.UserMapper;
import com.software_design.Restaurant.Management.System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    @Autowired
    private UserRepository repository;

    public boolean setActive(User user) {
        if (!findActive()) {
            user.setLogged(true);
            repository.save(user);
            return true;
        }
        return false;
    }

    public boolean findActive() {
        return getUsers().stream().anyMatch(User::isLogged);
    }

    public boolean isAdminLogged() {
        return getUsers().stream()
                .anyMatch(user -> "ADMIN".equalsIgnoreCase(user.getRole()) && user.isLogged());
    }

    public User saveAdmin(User user) throws NoSuchAlgorithmException {
        user.setPassword(Security.encryptPassword(user.getPassword()));
        user.setRole("ADMIN");
        return repository.save(user);
    }

    public User saveUser(User user) throws NoSuchAlgorithmException {
        user.setPassword(Security.encryptPassword(user.getPassword()));
        if (isAdminLogged()) {  // only the admin has the rights
            user.setRole(user.getRole().toString());
        }
        return repository.save(user);
    }

    public boolean validateUserCredentials(String username, String password) throws NoSuchAlgorithmException {
        User user = repository.findByUsername(username);
        if (user != null) {
            String hashedPassword = Security.encryptPassword(password);
            return hashedPassword.equals(user.getPassword());
        }
        return false;
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public List<UserDTO> getAllUsers() {
        return userMapper.userListEntityToDto(repository.findAll());
    }

    public User getUserById(int userId) {
        return repository.findById(userId).orElse(null);
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

}