package com.turkcell.mini_e_commere_hw2.controller;

import an.awesome.pipelinr.Pipeline;
import com.turkcell.mini_e_commere_hw2.application.cart.commands.update.AddToCartCommand;
import com.turkcell.mini_e_commere_hw2.application.cart.commands.update.RemoveFromCartCommand;
import com.turkcell.mini_e_commere_hw2.application.cart.queries.get.GetCartByIdQuery;
import com.turkcell.mini_e_commere_hw2.application.cart.queries.list.GetAllCartsQuery;
import com.turkcell.mini_e_commere_hw2.application.cart.queries.get.GetMyCartQuery;
import com.turkcell.mini_e_commere_hw2.core.web.BaseController;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartListingDto;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController extends BaseController {
    
    public CartController(Pipeline pipeline) {
        super(pipeline);
    }

    @PostMapping
    public void addCartItemToCart(@RequestBody @Valid AddToCartCommand command) {
        pipeline.send(command);
    }

    @DeleteMapping
    public void removeCartItemFromCart(@RequestBody @Valid RemoveFromCartCommand command) {
        pipeline.send(command);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin')")
    public List<CartListingDto> getAll() {
        return pipeline.send(new GetAllCartsQuery());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('customer')")
    public CartListingDto getMyCart() {
        return pipeline.send(new GetMyCartQuery());
    }

    @GetMapping("/{cartId}")
    public CartListingDto getProducts(@PathVariable Integer cartId) {
        return pipeline.send(new GetCartByIdQuery(cartId));
    }
}
