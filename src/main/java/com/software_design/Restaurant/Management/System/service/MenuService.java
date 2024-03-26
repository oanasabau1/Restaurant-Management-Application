package com.software_design.Restaurant.Management.System.service;

import com.software_design.Restaurant.Management.System.entity.Menu;
import com.software_design.Restaurant.Management.System.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class MenuService {
    private final OrderService service;
    @Autowired
    private MenuRepository repository;

    public Menu saveMenu(Menu menu) {
        return repository.save(menu);
    }

    public List<Menu> saveMenus(List<Menu> menus) {
        return repository.saveAll(menus);
    }

    public List<Menu> getMenus() {
        return repository.findAll();
    }

    public Menu getMenuByName(String name) {
        return repository.findByName(name);
    }

    public Menu getMenuById(int menuId) {
        return repository.findById(menuId).orElse(null);
    }

    public String deleteMenu(int menuId) {
        Menu menu = repository.findById(menuId).orElse(null);
        if (menu == null) {
            return "Menu with id " + menuId + " does not exist";
        }
        boolean isMenuMappedToOrder = service.isMenuMappedToOrder(menuId);
        if (isMenuMappedToOrder) {
            return "Cannot delete menu with id " + menuId + " because it is mapped to one or more orders";
        }
        repository.deleteById(menuId);
        return "Menu with id " + menuId + " removed from the menu";
    }

    public Menu updateMenu(Menu menu) {
        Menu existingMenu = repository.findById(menu.getMenuId()).orElse(null);
        if (existingMenu != null) {
            existingMenu.setName(menu.getName());
            existingMenu.setPrice(menu.getPrice());
            existingMenu.setStock(menu.getStock());
            return repository.save(existingMenu);
        } else {
            return null;
        }
    }
}