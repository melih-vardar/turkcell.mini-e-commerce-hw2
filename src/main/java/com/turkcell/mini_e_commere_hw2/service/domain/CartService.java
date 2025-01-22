package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.Cart;

import java.util.List;

public interface CartService {
    Cart getById(Integer id);
    List<Cart> getAll();
    void addCartItemToCart(Integer cartId, Integer productId, Integer quantity);
    void removeCartItemFromCart(Integer cartId, Integer cartItemId, Integer quantity);
    void resetCart(Integer cartId);
    boolean existsById(Integer id);
}
