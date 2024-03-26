package com.software_design.Restaurant.Management.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "orders_menu",
            joinColumns = {
                    @JoinColumn(name = "orderId",
                            referencedColumnName = "orderId")},
            inverseJoinColumns = {
                    @JoinColumn(name = "menuId",
                            referencedColumnName = "menuId")})
    private List<Menu> orderedMenu = new ArrayList<>();
    ;
    private double totalPrice;
    private String status;
    private LocalDateTime time;
}
