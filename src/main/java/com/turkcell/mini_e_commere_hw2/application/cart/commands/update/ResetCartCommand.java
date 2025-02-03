package com.turkcell.mini_e_commere_hw2.application.cart.commands.update;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.repository.CartRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
public class ResetCartCommand implements Command<Void> {

    private Integer cartId;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<ResetCartCommand, Void> {

        private final CartRepository cartRepository;


        @Override
        public Void handle(ResetCartCommand command) {


            Cart cart = cartRepository.findById(
                            command.getCartId())
                    .orElseThrow(() -> new BusinessException("Cart not found"));

            cart.getCartItems().clear();
            cart.setTotalPrice(BigDecimal.ZERO);
            cartRepository.save(cart);
            return null;
        }
    }
}
