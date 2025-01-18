package com.turkcell.mini_e_commere_hw2.rules;

import com.turkcell.mini_e_commere_hw2.repository.SubCategoryRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class SubCategoryBusinessRules {
    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryBusinessRules(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    public void subCategoryMustExist(Integer id) {
        if (!subCategoryRepository.existsById(id))
            throw new BusinessException("SubCategory not found");
    }
} 