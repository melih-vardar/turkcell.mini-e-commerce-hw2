package com.turkcell.mini_e_commere_hw2.application.cart.commands.update;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.CartItem;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.CartRepository;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import com.turkcell.mini_e_commere_hw2.rules.CartBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.ProductBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

@Getter
@Setter
public class AddToCartCommand implements Command<Void> {
    @NotNull(message = "Product ID cannot be null")
    private Integer productId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @Component
    public static class Handler implements Command.Handler<AddToCartCommand, Void> {
        private final CartRepository cartRepository;
        private final ProductRepository productRepository;
        private final ProductBusinessRules productBusinessRules;
        private final UserRepository userRepository;

        public Handler(CartRepository cartRepository, 
                      ProductRepository productRepository,
                      CartBusinessRules cartBusinessRules,
                      ProductBusinessRules productBusinessRules,
                       UserRepository userRepository) {
            this.cartRepository = cartRepository;
            this.productRepository = productRepository;
            this.productBusinessRules = productBusinessRules;
            this.userRepository = userRepository;
        }

        @Override
        public Void handle(AddToCartCommand command) {
            // Business Rules
            productBusinessRules.productIdMustExist(command.getProductId());
            productBusinessRules.productMustBeInStock(command.getProductId(), command.getQuantity());

            Product product = productRepository.findById(command.getProductId())
                .orElseThrow(() -> new BusinessException("Product not found"));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null || !authentication.isAuthenticated()) {
                throw new BusinessException("No authenticated user found");
            }

            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BusinessException("User not found"));

            Cart cart = cartRepository.findByUserId(
                    user.getId())
                .orElseThrow(() -> new BusinessException("Cart not found"));

            // Check if product exists in cart
            CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(command.getProductId()))
                .findFirst()
                .orElse(null);

            BigDecimal itemPrice = product.getUnitPrice().multiply(BigDecimal.valueOf(command.getQuantity()));

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + command.getQuantity());
                existingItem.setPrice(existingItem.getPrice().add(itemPrice));
            } else {
                CartItem newItem = new CartItem();
                newItem.setProduct(product);
                newItem.setCart(cart);
                newItem.setQuantity(command.getQuantity());
                newItem.setPrice(itemPrice);
                cart.getCartItems().add(newItem);
            }

            cart.setTotalPrice(cart.getTotalPrice().add(itemPrice));
            cartRepository.save(cart);

            return null;
        }
    }
} 