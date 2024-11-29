package com.backend.ecommercebackend.repository.order;

import com.backend.ecommercebackend.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
    @Query("SELECT o.id FROM Order o WHERE o.orderStatus = 'Gözləyir'")
    List<Long> findOrderIdsByStatusGozleyir();

    @Query("SELECT o.id FROM Order o WHERE o.orderStatus = 'İmtina'")
    List<Long> findOrderIdsByStatusImtina();

    @Query("SELECT o.id FROM Order o WHERE o.orderStatus = 'Çatdırılıb'")
    List<Long> findOrderIdsByStatusCatdirilib();
}
