package com.turkcell.mini_e_commere_hw2.repository;

import com.turkcell.mini_e_commere_hw2.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
