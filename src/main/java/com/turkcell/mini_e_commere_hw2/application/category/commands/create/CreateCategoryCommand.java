package com.turkcell.mini_e_commere_hw2.application.category.commands.create;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
public class CreateCategoryCommand implements Command<CreatedCategoryResponse> {
    @NotBlank(message = "Category name cannot be empty")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    private String name;
    
    private Integer parentId;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<CreateCategoryCommand, CreatedCategoryResponse> {

        private final CategoryRepository categoryRepository;

        @Override
        public CreatedCategoryResponse handle(CreateCategoryCommand command) {
            if (categoryRepository.findByName(command.getName()).isPresent()) {
                throw new BusinessException("Category with this name already exists");
            }

            Category category = new Category();
            category.setName(command.getName());
            
            if (command.getParentId() != null) {
                Category parent = categoryRepository.findById(command.getParentId())
                    .orElseThrow(() -> new BusinessException("Parent category not found"));
                category.setParent(parent);
            }

            categoryRepository.save(category);
            return new CreatedCategoryResponse(category.getName());
        }
    }
} 