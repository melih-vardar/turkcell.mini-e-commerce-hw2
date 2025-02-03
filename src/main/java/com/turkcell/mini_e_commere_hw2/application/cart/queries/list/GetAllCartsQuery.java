package com.turkcell.mini_e_commere_hw2.application.cart.queries.list;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartItemDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.repository.CartRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GetAllCartsQuery implements Command<List<CartListingDto>> {

    @Component
    public static class Handler implements Command.Handler<GetAllCartsQuery, List<CartListingDto>> {
        private final CartRepository cartRepository;

        public Handler(CartRepository cartRepository) {
            this.cartRepository = cartRepository;
        }

        @Override
        public List<CartListingDto> handle(GetAllCartsQuery query) {
            List<Cart> carts = cartRepository.findAll();
            return carts.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }

        private CartListingDto mapToDto(Cart cart) {
            CartListingDto dto = new CartListingDto();
            // Map cart properties to dto
            dto.setId(cart.getId());
            dto.setUserId(cart.getUser().getId());
            dto.setCartItems(cart.getCartItems().stream()
                    .map(cartItem -> new CartItemDto(
                            cartItem.getId(),
                            cartItem.getProduct().getId(),
                            cartItem.getQuantity(),
                            cartItem.getPrice()))
                    .toList());
            dto.setTotalPrice(cart.getTotalPrice());
            return dto;
        }
    }
} 