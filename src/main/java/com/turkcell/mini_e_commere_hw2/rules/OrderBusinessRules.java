package com.turkcell.mini_e_commere_hw2.rules;

import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.CartItem;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.repository.OrderRepository;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class OrderBusinessRules {
    private final ProductRepository productRepository;

    public void checkTheProductStockAfterUpdateProductStockForOrder(Cart cart){
        for (CartItem cartItem : cart.getCartItems()){
            Optional<Product> product = productRepository.findById(cartItem.getProduct().getId());

            if(product.isEmpty()){
                throw new BusinessException("Product not found with id: " + cartItem.getProduct().getId());
            }

            if(product.get().getStock() < cartItem.getQuantity()){
                throw new BusinessException("Insufficient stock for product: " + product.get().getName() +
                        ". Available stock: " + product.get().getStock() + ", Required: " + cartItem.getQuantity());
            }

            product.get().setStock(product.get().getStock()-cartItem.getQuantity());
            productRepository.save(product.get());
        }
    }
    // kullanicinin sepeti bos ise siparis olusturulamaz
    public void cartMustNotBeEmpty(Cart cart){
        if(cart.getCartItems() == null || cart.getCartItems().isEmpty()){
            throw new BusinessException("Cart is empty. Cannot create order.");
        }
    }
}
