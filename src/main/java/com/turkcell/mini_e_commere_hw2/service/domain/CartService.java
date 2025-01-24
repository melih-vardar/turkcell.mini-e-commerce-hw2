package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.Cart;

import java.util.List;
import java.util.UUID;

public interface CartService {
    Cart getById(Integer id);
    List<Cart> getAll();
    void addCartItemToCart(UUID userId, Integer productId, Integer quantity);
    void removeCartItemFromCart(UUID userId, Integer cartItemId, Integer quantity);
    void resetCart(Integer cartId);
    boolean existsById(Integer id);
    int getCartIdByUserId(UUID userId);

    Object getMyCart(UUID activeUserId);
}
