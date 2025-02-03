package com.turkcell.mini_e_commere_hw2.application.category.queries.list;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.rules.CategoryBusinessRules;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSubCategoriesQuery implements Command<List<CategoryListingDto>> {
    @NotNull(message = "Parent category ID cannot be null")
    private Integer parentId;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<GetSubCategoriesQuery, List<CategoryListingDto>> {
        private final CategoryRepository categoryRepository;
        private final CategoryBusinessRules categoryBusinessRules;


        @Override
        public List<CategoryListingDto> handle(GetSubCategoriesQuery query) {
            categoryBusinessRules.categoryMustExist(query.getParentId());
            List<Category> subCategories = categoryRepository.findAllByParentId(query.getParentId());
            return subCategories.stream()
                    .map(this::mapToDto)
                    .toList();
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