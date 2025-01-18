package com.turkcell.mini_e_commere_hw2.repository;


import com.turkcell.mini_e_commere_hw2.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
