package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.cart.AddToCartDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartListingDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.RemoveFromCartDto;

import java.util.List;

public interface CartApplicationService {
    CartListingDto getById(Integer id);
    List<CartListingDto> getAll();
    void addCartItemToCart(AddToCartDto addToCartDto);
    void removeCartItemFromCart(RemoveFromCartDto removeFromCartDto);
    void resetCart(Integer cartId);

    CartListingDto getMyCart();
}
