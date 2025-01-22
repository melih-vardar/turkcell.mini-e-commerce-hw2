package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.CartItem;
import com.turkcell.mini_e_commere_hw2.repository.CartItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartItemService{
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem getById(Integer id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public void delete(Integer id) {
        cartItemRepository.deleteById(id);
    }
}
