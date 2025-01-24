package com.turkcell.mini_e_commere_hw2.rules;

import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryBusinessRules {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public void categoryMustExist(Integer id) {
        categoryRepository.findById(id).orElseThrow(() -> new BusinessException("Category not found"));
    }

    public void categoryMustNotHaveAssociatedProducts(Integer categoryId) {
        if (productRepository.existsByCategoryId(categoryId)) {
            throw new BusinessException("Category cannot be deleted because it has associated products.");
        }
    }

    public void categoryMustNotHaveSubCategories(Integer categoryId) {
        if (categoryRepository.existsByParentId(categoryId)) {
            throw new BusinessException("Category cannot be deleted because it has sub-categories.");
        }
    }
}
