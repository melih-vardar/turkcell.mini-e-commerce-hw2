package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.cart.AddToCartDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartListingDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.RemoveFromCartDto;
import com.turkcell.mini_e_commere_hw2.service.application.CartApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/carts")
@AllArgsConstructor
public class CartController {
    private final CartApplicationService cartApplicationService;

    @PostMapping()
    public void addCartItemToCart(@RequestBody AddToCartDto addToCartDto) {
        this.cartApplicationService.addCartItemToCart(addToCartDto);
    }

    @DeleteMapping()
    public void removeCartItemFromCart(@RequestBody RemoveFromCartDto removeFromCartDto) {
        this.cartApplicationService.removeCartItemFromCart(removeFromCartDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin')")
    public List<CartListingDto> getAll() {
        return this.cartApplicationService.getAll();
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('customer')")
    public CartListingDto getMyCart() {
        return this.cartApplicationService.getMyCart();
    }

    @GetMapping("/{cartId}")
    public CartListingDto getProducts(@PathVariable Integer cartId) {
        return this.cartApplicationService.getById(cartId);
    }
}
