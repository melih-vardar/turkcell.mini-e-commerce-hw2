package com.turkcell.mini_e_commere_hw2.application.category.queries.get;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class GetCategoryByIdQuery implements Command<CategoryListingDto> {
    @NotNull(message = "Category ID cannot be null")
    private Integer id;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<GetCategoryByIdQuery, CategoryListingDto> {
        private final CategoryRepository categoryRepository;

        @Override
        public CategoryListingDto handle(GetCategoryByIdQuery query) {
            Category category = categoryRepository.findByIdWithDetails(query.getId())
                    .orElseThrow(() -> new BusinessException("Category not found"));

            return mapToDto(category);
        }

        private CategoryListingDto mapToDto(Category category) {
            CategoryListingDto dto = new CategoryListingDto();
            dto.setId(category.getId());
            dto.setName(category.getName());
            
            if (category.getParent() != null) {
                dto.setParentId(category.getParent().getId());
                dto.setParentName(category.getParent().getName());
            }
            
            dto.setSubCategories(category.getSubCategories() != null ?
                    category.getSubCategories().stream()
                            .map(this::mapToDto)
                            .toList() :
                    new ArrayList<>());
            
            dto.setProductCount(category.getProducts() != null ? 
                    category.getProducts().size() : 0);
            
            return dto;
        }
    }
} 