package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.repository.SubCategoryRepository;
import com.turkcell.mini_e_commere_hw2.rules.CategoryBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryBusinessRules categoryBusinessRules;

    @Override
    public void add(SubCategory subCategory) {
        categoryBusinessRules.categoryMustExist(subCategory.getCategory().getId());

        Category category = categoryRepository
                .findById(subCategory.getCategory().getId())
                .orElse(null);

        SubCategory subCategoryWithSameName = subCategoryRepository
                .findByName(subCategory.getName())
                .orElse(null);

        if (subCategoryWithSameName != null)
            throw new BusinessException("SubCategory already exists");

        subCategoryRepository.save(subCategory);
    }

    @Override
    public void update(SubCategory subCategory) {
        categoryBusinessRules.categoryMustExist(subCategory.getCategory().getId());

        Category category = categoryRepository
                .findById(subCategory.getCategory().getId())
                .orElse(null);

        SubCategory subCategoryWithSameName = subCategoryRepository
                .findByNameIsAndIdIsNot(subCategory.getName(), subCategory.getId())
                .orElse(null);

        if (subCategoryWithSameName != null)
            throw new BusinessException("SubCategory already exists");

        SubCategory subCategoryToUpdate = subCategoryRepository.findById(subCategory.getId())
                .orElseThrow(() -> new BusinessException("SubCategory not found"));

        subCategoryRepository.save(subCategoryToUpdate);
    }

    @Override
    public List<SubCategory> getAll() {
        return subCategoryRepository.findAll();
    }

    @Override
    public List<SubCategory> getAllByCategoryId(Integer categoryId) {
        return subCategoryRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public Optional<SubCategory> findById(Integer id) {
        return subCategoryRepository.findById(id);
    }
} 