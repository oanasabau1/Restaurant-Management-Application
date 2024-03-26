package com.software_design.Restaurant.Management.System.repository;

import ch.qos.logback.core.status.Status;
import com.software_design.Restaurant.Management.System.entity.Menu;
import com.software_design.Restaurant.Management.System.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByStatus(Status name);

    @Query("SELECT COUNT(o) > 0 FROM Order o JOIN o.orderedMenu m WHERE m.menuId = :menuId")
    boolean existsOrderByMenuId(@Param("menuId") int menuId);

    @Query("SELECT o FROM Order o WHERE o.time BETWEEN :startDate AND :endDate")
    List<Order> findByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m, COUNT(o) AS numOrders " +
            "FROM Order o " +
            "JOIN o.orderedMenu m " +
            "GROUP BY m " +
            "ORDER BY numOrders DESC")
    List<Menu> findMostOrderedMenus();
}
