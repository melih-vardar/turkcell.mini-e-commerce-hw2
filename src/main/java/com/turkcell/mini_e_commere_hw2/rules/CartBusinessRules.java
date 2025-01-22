package com.turkcell.mini_e_commere_hw2.rules;

import com.turkcell.mini_e_commere_hw2.repository.CartItemRepository;
import com.turkcell.mini_e_commere_hw2.repository.CartRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartBusinessRules {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public void cartIdMustExist(Integer id)
    {
        cartRepository.findById(id).orElseThrow(() -> new BusinessException("Cart not found"));
    }

    public void cartItemMustExist(Integer id)
    {
        cartItemRepository.findById(id).orElseThrow(() -> new BusinessException("Cart item not found"));
    }
}