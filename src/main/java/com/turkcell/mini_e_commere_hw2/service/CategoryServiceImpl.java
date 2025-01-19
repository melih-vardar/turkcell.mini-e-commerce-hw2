package com.turkcell.mini_e_commere_hw2.service;
import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.category.CreateCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.category.UpdateCategoryDto;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
  private final CategoryRepository categoryRepository;

  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public void add(CreateCategoryDto createCategoryDto) {
    //TODO: Implement this method
    throw new NotImplementedException("Not implemented yet");
  }

  @Override
  public void update(UpdateCategoryDto updateCategoryDto) {
    //TODO: Implement this method
    throw new NotImplementedException("Not implemented yet");
  }

  @Override
  public void delete(Integer id) {
    //TODO: Implement this method
    throw new NotImplementedException("Not implemented yet");
  }

  @Override
  public List<CategoryListingDto> getAll() {
    //TODO: Implement this method
    throw new NotImplementedException("Not implemented yet");
  }

  @Override
  public CategoryListingDto getById(Integer id) {
    //TODO: Implement this method
    throw new NotImplementedException("Not implemented yet");
  }
}
