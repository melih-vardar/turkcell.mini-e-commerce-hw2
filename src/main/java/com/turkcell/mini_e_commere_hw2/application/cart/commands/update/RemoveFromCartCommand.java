package com.turkcell.mini_e_commere_hw2.application.cart.commands.update;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.CartItem;
import com.turkcell.mini_e_commere_hw2.repository.CartItemRepository;
import com.turkcell.mini_e_commere_hw2.repository.CartRepository;
import com.turkcell.mini_e_commere_hw2.rules.CartBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RemoveFromCartCommand implements Command<Void> {
    @NotNull(message = "Cart Item ID cannot be null")
    private Integer cartItemId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @Component
    public static class Handler implements Command.Handler<RemoveFromCartCommand, Void> {
        private final CartRepository cartRepository;
        private final CartItemRepository cartItemRepository;
        private final CartBusinessRules cartBusinessRules;

        public Handler(CartRepository cartRepository,
                      CartItemRepository cartItemRepository,
                      CartBusinessRules cartBusinessRules) {
            this.cartRepository = cartRepository;
            this.cartItemRepository = cartItemRepository;
            this.cartBusinessRules = cartBusinessRules;
        }

        @Override
        public Void handle(RemoveFromCartCommand command) {
            cartBusinessRules.cartItemMustExist(command.getCartItemId());

            CartItem cartItem = cartItemRepository.findById(command.getCartItemId())
                .orElseThrow(() -> new BusinessException("Cart item not found"));

            Cart cart = cartItem.getCart();

            if (cartItem.getQuantity() <= command.getQuantity()) {
                cart.getCartItems().remove(cartItem);
                cart.setTotalPrice(cart.getTotalPrice().subtract(cartItem.getPrice()));
                cartItemRepository.delete(cartItem);
            } else {
                BigDecimal priceReduction = cartItem.getProduct().getUnitPrice()
                    .multiply(BigDecimal.valueOf(command.getQuantity()));
                cartItem.setQuantity(cartItem.getQuantity() - command.getQuantity());
                cartItem.setPrice(cartItem.getPrice().subtract(priceReduction));
                cart.setTotalPrice(cart.getTotalPrice().subtract(priceReduction));
            }

            cartRepository.save(cart);
            return null;
        }
    }
} 