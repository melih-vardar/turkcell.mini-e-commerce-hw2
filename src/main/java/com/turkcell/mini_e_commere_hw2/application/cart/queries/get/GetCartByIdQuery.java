package com.turkcell.mini_e_commere_hw2.application.cart.queries.get;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartItemDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.repository.CartRepository;
import com.turkcell.mini_e_commere_hw2.rules.CartBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCartByIdQuery implements Command<CartListingDto> {
    @NotNull(message = "Cart ID cannot be null")
    private Integer id;

    @Component
    public static class Handler implements Command.Handler<GetCartByIdQuery, CartListingDto> {
        private final CartRepository cartRepository;

        public Handler(CartRepository cartRepository) {
            this.cartRepository = cartRepository;
        }

        @Override
        public CartListingDto handle(GetCartByIdQuery query) {

            Cart cart = cartRepository.findById(query.getId())
                    .orElseThrow(() -> new BusinessException("Cart not found"));

            List<CartItemDto> cartItemDtoList = cart.getCartItems()
                    .stream()
                    .map(cartItem -> new CartItemDto(
                            cartItem.getId(),
                            cartItem.getProduct().getId(),
                            cartItem.getQuantity(),
                            cartItem.getPrice()))
                    .toList();

            CartListingDto cartListingDto = new CartListingDto();

            cartListingDto.setId(cart.getId());
            cartListingDto.setUserId(cart.getUser().getId());
            cartListingDto.setCartItems(cartItemDtoList);
            cartListingDto.setTotalPrice(cart.getTotalPrice());

            return cartListingDto;

        }
    }
} 