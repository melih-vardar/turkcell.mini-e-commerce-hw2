package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.CartItem;

public interface CartItemService {
    CartItem getById(Integer id);
    void delete(Integer id);
}
