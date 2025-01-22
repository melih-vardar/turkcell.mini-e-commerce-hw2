package com.turkcell.mini_e_commere_hw2.rules;


import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.repository.CartRepository;
import com.turkcell.mini_e_commere_hw2.repository.OrderRepository;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductBusinessRules {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public void productIdMustExist(int productId) {
        productRepository.findById(productId).orElseThrow(() -> new BusinessException("Product not found"));
    }

    public void productNameMustExist(String name) {
        productRepository.findByName(name).ifPresent(product -> {
            throw new BusinessException("Product name already exists");
        });
    }

    public void productMustNotBeAssociatedWithAnyOrder(Integer id) {
        if (orderRepository.existsByProductId(id)) {
            throw new BusinessException("Product cannot be deleted because it is associated with orders");
        }
    }

    public void productMustBeInStock(Integer id, Integer quantity) {
        if (productRepository.findById(id).get().getStock() < quantity) {
            throw new BusinessException("Product is not in stock");
        }
    }

    //Sepette aynı üründen varsa, eklenen miktar stok miktarını aşmamalı
    public void productMustBeInStockInCart(Integer productId, Integer quantity, Integer cartId) {
        if (cartRepository.existsById(cartId)) {
            Cart cart = cartRepository.findById(cartId).get();
            if (cart.getCartItems().stream().filter(cartItem -> cartItem.getProduct().equals(productId)).findFirst().get().getQuantity() + quantity > productRepository.findById(productId).get().getStock()) {
                throw new BusinessException("Product is not in stock");
            }
        }
    }


}
