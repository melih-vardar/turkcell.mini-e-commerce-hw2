package com.turkcell.mini_e_commere_hw2.repository;


import com.turkcell.mini_e_commere_hw2.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    boolean existsByProductId(Integer productId);
}
