package com.turkcell.mini_e_commere_hw2.application.category.commands.update;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.rules.CategoryBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class UpdateCategoryCommand implements Command<Void> {
    @NotNull(message = "Category ID cannot be null")
    private Integer id;

    @NotBlank(message = "Category name cannot be empty")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    private String name;
    
    private Integer parentId;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<UpdateCategoryCommand, Void> {
        private final CategoryRepository categoryRepository;
        private final CategoryBusinessRules categoryBusinessRules;

        @Override
        public Void handle(UpdateCategoryCommand command) {
            categoryBusinessRules.categoryMustExist(command.getId());

            Category category = categoryRepository.findById(command.getId())
                .orElseThrow(() -> new BusinessException("Category not found"));

            if (!category.getName().equals(command.getName()) && 
                categoryRepository.findByName(command.getName()).isPresent()) {
                throw new BusinessException("Category with this name already exists");
            }

            category.setName(command.getName());
            
            if (command.getParentId() != null) {
                if (command.getId().equals(command.getParentId())) {
                    throw new BusinessException("A category cannot be its own parent");
                }
                Category parent = categoryRepository.findById(command.getParentId())
                    .orElseThrow(() -> new BusinessException("Parent category not found"));
                category.setParent(parent);
            }

            categoryRepository.save(category);
            return null;
        }
    }
} 