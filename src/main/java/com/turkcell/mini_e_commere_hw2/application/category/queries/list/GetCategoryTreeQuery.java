package com.turkcell.mini_e_commere_hw2.application.category.queries.list;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class GetCategoryTreeQuery implements Command<List<CategoryListingDto>> {

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<GetCategoryTreeQuery, List<CategoryListingDto>> {
        private final CategoryRepository categoryRepository;

        @Override
        public List<CategoryListingDto> handle(GetCategoryTreeQuery query) {
            List<Category> rootCategories = categoryRepository.findAllRootCategories();
            return rootCategories.stream()
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