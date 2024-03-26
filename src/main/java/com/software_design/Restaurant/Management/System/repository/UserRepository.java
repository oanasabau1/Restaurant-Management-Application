package com.software_design.Restaurant.Management.System.repository;

import com.software_design.Restaurant.Management.System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
