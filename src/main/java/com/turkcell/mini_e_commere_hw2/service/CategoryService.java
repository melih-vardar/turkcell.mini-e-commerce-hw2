package com.turkcell.mini_e_commere_hw2.service;



import com.turkcell.mini_e_commere_hw2.entity.Category;

import java.util.Optional;

public interface CategoryService {
  Optional<Category> findById(Integer id);
}
