package com.turkcell.mini_e_commere_hw2.application.cart.queries.get;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartItemDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.CartRepository;
import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@NoArgsConstructor
public class GetMyCartQuery implements Command<CartListingDto> {

    @Component
    public static class Handler implements Command.Handler<GetMyCartQuery, CartListingDto> {
        private final CartRepository cartRepository;
        private final UserRepository userRepository;


        public Handler(CartRepository cartRepository, UserRepository userRepository) {

            this.cartRepository = cartRepository;
            this.userRepository = userRepository;
        }

        @Override
        public CartListingDto handle(GetMyCartQuery query) {

            // Get active user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null || !authentication.isAuthenticated()) {
                throw new BusinessException("No authenticated user found");
            }

            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BusinessException("User not found"));

            Cart cart = cartRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new BusinessException("Cart not found"));


            return mapToDto(cart);
        }

        private CartListingDto mapToDto(Cart cart) {
            CartListingDto dto = new CartListingDto();
            dto.setId(cart.getId());
            dto.setUserId(cart.getUser().getId());
            dto.setCartItems(cart.getCartItems().stream()
                    .map(cartItem -> new
                            CartItemDto(cartItem.getId(),
                            cartItem.getProduct().getId(),
                            cartItem.getQuantity(),
                            cartItem.getPrice()))
                    .toList());
            dto.setTotalPrice(cart.getTotalPrice());
            return dto;
        }
    }
} 