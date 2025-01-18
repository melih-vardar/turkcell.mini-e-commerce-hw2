package com.turkcell.mini_e_commere_hw2.service;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
  private final CategoryRepository categoryRepository;

  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Optional<Category> findById(Integer id) {
    return categoryRepository.findById(id);
  }
}
