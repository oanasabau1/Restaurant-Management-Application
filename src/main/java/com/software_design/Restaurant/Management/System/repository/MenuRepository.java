package com.software_design.Restaurant.Management.System.repository;

import com.software_design.Restaurant.Management.System.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Menu findByName(String name);
}
