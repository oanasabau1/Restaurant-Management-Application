package com.software_design.Restaurant.Management.System.controller;

import com.software_design.Restaurant.Management.System.entity.Menu;
import com.software_design.Restaurant.Management.System.service.MenuService;
import com.software_design.Restaurant.Management.System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;
    private final UserService userService;

    @Autowired
    public MenuController(MenuService menuService, UserService userService) {
        this.menuService = menuService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMenu(@RequestBody Menu menu) {
        if (!userService.isAdminLogged()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can add a new menu.");
        }

        Menu existingMenu = menuService.getMenuByName(menu.getName());
        if (existingMenu != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Menu with this name already exists.");
        }

        Menu savedMenu = menuService.saveMenu(menu);
        return ResponseEntity.ok(savedMenu);
    }

    @PostMapping("/addMenus")
    public ResponseEntity<?> addMenus(@RequestBody List<Menu> menus) {
        if (!userService.isAdminLogged()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can add new menus.");
        }

        List<Menu> savedMenus = menuService.saveMenus(menus);
        return ResponseEntity.ok(savedMenus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findMenuById(@PathVariable int id) {
        Menu menu = menuService.getMenuById(id);
        if (menu == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menu);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findMenuByName(@PathVariable String name) {
        Menu menu = menuService.getMenuByName(name);
        if (menu == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menu);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMenus() {
        List<Menu> menus = menuService.getMenus();
        return ResponseEntity.ok(menus);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMenu(@RequestBody Menu menu) {
        if (!userService.isAdminLogged()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can update menus.");
        }
        Menu updatedMenu = menuService.updateMenu(menu);
        if (updatedMenu == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMenu);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable int id) {
        if (!userService.isAdminLogged()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can delete menus.");
        }
        String deletionMessage = menuService.deleteMenu(id);
        return ResponseEntity.ok(deletionMessage);
    }
}
