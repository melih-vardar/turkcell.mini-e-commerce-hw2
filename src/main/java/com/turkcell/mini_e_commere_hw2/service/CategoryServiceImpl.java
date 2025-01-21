package com.turkcell.mini_e_commere_hw2.service;
import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.category.CreateCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.category.UpdateCategoryDto;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.repository.SubCategoryRepository;
import com.turkcell.mini_e_commere_hw2.rules.CategoryBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{
  private final CategoryRepository categoryRepository;
  private final CategoryBusinessRules categoryBusinessRules;
  private final SubCategoryRepository subCategoryRepository;
  private final ProductRepository productRepository;

  @Override
  public void add(CreateCategoryDto createCategoryDto) {
    Category categoryWithSameName = categoryRepository
            .findByName(createCategoryDto.getName())
            .orElse(null);

    if (categoryWithSameName != null)
      throw new BusinessException("Category already exists");

    Category category = new Category();
    category.setName(createCategoryDto.getName());
    categoryRepository.save(category);
  }

  @Override
  public void update(UpdateCategoryDto updateCategoryDto) {
    categoryBusinessRules.categoryMustExist(updateCategoryDto.getId());

    Category categoryToUpdate = categoryRepository.findById(updateCategoryDto.getId())
            .orElseThrow(() -> new BusinessException("Category not found"));

    categoryToUpdate.setName(updateCategoryDto.getName());
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
  public List<CategoryListingDto> getAll() {
    return categoryRepository
            .findAll()
            .stream()
            .map(category -> new CategoryListingDto(
                    category.getId(),
                    category.getName(),
                    category.getSubCategories() != null ? category.getSubCategories().size() : 0,
                    category.getSubCategories() != null
                            ? category.getSubCategories().stream()
                            .map(SubCategory::getName)
                            .collect(Collectors.toList())
                            : Collections.emptyList()
            ))
            .toList();
  }

  @Override
  public CategoryListingDto getById(Integer id) {
    categoryBusinessRules.categoryMustExist(id);

    return categoryRepository
            .findById(id)
            .map(category -> new CategoryListingDto(
                    category.getId(),
                    category.getName(),
                    category.getSubCategories() != null ? category.getSubCategories().size() : 0,
                    category.getSubCategories() != null
                            ? category.getSubCategories().stream()
                            .map(SubCategory::getName)
                            .collect(Collectors.toList())
                            : Collections.emptyList()
            ))
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
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
