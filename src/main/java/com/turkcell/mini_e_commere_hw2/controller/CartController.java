package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.cart.AddToCartDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartListingDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.RemoveFromCartDto;
import com.turkcell.mini_e_commere_hw2.service.application.CartApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/carts")
@AllArgsConstructor
public class CartController {
    private final CartApplicationService cartApplicationService;

    @PostMapping("/{cartId}")
    public void addCartItemToCart(@PathVariable Integer cartId, @RequestBody AddToCartDto addToCartDto) {
        this.cartApplicationService.addCartItemToCart(cartId, addToCartDto);
    }

    @DeleteMapping("/{cartId}")
    public void removeCartItemFromCart(@PathVariable Integer cartId, @RequestBody RemoveFromCartDto removeFromCartDto) {
        this.cartApplicationService.removeCartItemFromCart(cartId, removeFromCartDto);
    }

    @GetMapping
    public List<CartListingDto> getAll() {
        return this.cartApplicationService.getAll();
    }

    @GetMapping("/{cartId}")
    public CartListingDto getProducts(@PathVariable Integer cartId) {
        return this.cartApplicationService.getById(cartId);
    }
}
