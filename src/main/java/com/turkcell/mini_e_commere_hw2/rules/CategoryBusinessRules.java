package com.turkcell.mini_e_commere_hw2.rules;

import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.repository.SubCategoryRepository;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CategoryBusinessRules {
  private final CategoryRepository categoryRepository;
  private final SubCategoryRepository subCategoryRepository;
  private final ProductRepository productRepository;

  public void categoryMustExist(Integer id)
  {
    categoryRepository.findById(id).orElseThrow(() -> new BusinessException("Category not found"));
  }

  public void categoryMustNotHaveAssociatedProducts(Integer categoryId)
  {
    List<SubCategory> subCategories = subCategoryRepository.findAllByCategoryId(categoryId);
    
    for (SubCategory subCategory : subCategories) {
      if (productRepository.existsBySubCategoryId(subCategory.getId())) {
        throw new BusinessException("Category cannot be deleted because it has associated products through subcategories.");
      }
    }
  }
}
