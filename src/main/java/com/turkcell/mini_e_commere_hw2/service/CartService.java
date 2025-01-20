package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.cart.CartListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Cart;

import java.util.List;

public interface CartService {
    CartListingDto getById(Integer id);
    List<CartListingDto> getAll();

    void addCartItemToCart(Integer cartId, Integer productId, Integer quantity);
    void removeCartItemFromCart(Integer cartId, Integer cartItemId, Integer quantity);
    public void saveCard(Cart cart);
}
