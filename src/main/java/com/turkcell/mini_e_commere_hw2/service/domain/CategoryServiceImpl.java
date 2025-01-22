package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.repository.SubCategoryRepository;
import com.turkcell.mini_e_commere_hw2.rules.CategoryBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{
  private final CategoryRepository categoryRepository;
  private final CategoryBusinessRules categoryBusinessRules;
  private final SubCategoryRepository subCategoryRepository;

  @Override
  public void add(Category category) {
    Category categoryWithSameName = categoryRepository
            .findByName(category.getName())
            .orElse(null);

    if (categoryWithSameName != null)
      throw new BusinessException("Category already exists");

    categoryRepository.save(category);
  }

  @Override
  public void update(Category category) {
    categoryBusinessRules.categoryMustExist(category.getId());

    Category categoryToUpdate = categoryRepository.findById(category.getId())
            .orElseThrow(() -> new BusinessException("Category not found"));

    categoryRepository.save(categoryToUpdate);
  }

  @Override
  public void delete(Integer id) {
    categoryBusinessRules.categoryMustExist(id);
    categoryBusinessRules.categoryMustNotHaveAssociatedProducts(id);

    Category categoryToUpdate = categoryRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Category not found"));

    categoryRepository.delete(categoryToUpdate);
  }

  @Override
  public List<Category> getAll() {
    return categoryRepository.findAll();
  }

  @Override
  public Category getById(Integer id) {
    categoryBusinessRules.categoryMustExist(id);

    return categoryRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Category not found"));
  }

  @Override
  public void addSubCategory(Integer categoryId, Integer subCategoryId) {
    categoryBusinessRules.categoryMustExist(subCategoryId);

    Category categoryToAddSubcategory = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new BusinessException("Category not found"));

    SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
            .orElseThrow(() -> new BusinessException("SubCategory not found"));

    if (categoryToAddSubcategory.getSubCategories() == null)
      categoryToAddSubcategory.setSubCategories(new ArrayList<>());

    categoryToAddSubcategory.getSubCategories().add(subCategory);

    categoryRepository.save(categoryToAddSubcategory);
  }
}
