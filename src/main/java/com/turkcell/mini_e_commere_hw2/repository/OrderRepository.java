package com.turkcell.mini_e_commere_hw2.repository;


import com.turkcell.mini_e_commere_hw2.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o " +
           "JOIN o.orderItems oi WHERE oi.product.id = :productId")
    boolean existsByProductId(@Param("productId") Integer productId);
}
