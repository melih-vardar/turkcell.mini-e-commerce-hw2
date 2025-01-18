package com.turkcell.mini_e_commere_hw2.rules;


import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class CategoryBusinessRules {
  private final CategoryRepository categoryRepository;

    public CategoryBusinessRules(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

  public void categoryMustExist(Integer id)
  {
    categoryRepository.findById(id).orElseThrow(() -> new BusinessException("Category not found"));
  }
}
