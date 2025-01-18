package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.cart.CartListingDto;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{cartId}/add")
    public void addCartItemToCart(@PathVariable Integer cartId, @RequestParam Integer productId, @RequestParam Integer quantity) {
        this.cartService.addCartItemToCart(cartId, productId, quantity);
    }

    @PostMapping("/{cartId}/remove")
    public void removeCartItemFromCart(@PathVariable Integer cartId, @RequestParam Integer cartItemId, @RequestParam Integer quantity) {
        this.cartService.removeCartItemFromCart(cartId, cartItemId, quantity);
    }

    @GetMapping
    public List<CartListingDto> getAll() {
        return this.cartService.getAll();
    }

    @GetMapping("/{cartId}")
    public CartListingDto getProducts(@PathVariable Integer cartId) {
        return this.cartService.getById(cartId);
    }



}
