package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.subcategory.CreateSubCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.SubCategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.UpdateSubCategoryDto;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import com.turkcell.mini_e_commere_hw2.service.domain.SubCategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubCategoryApplicationServiceImpl implements SubCategoryApplicationService {
    private final SubCategoryService subCategoryService;
    private final ModelMapper modelMapper;

    @Override
    public void add(CreateSubCategoryDto createSubCategoryDto) {
        SubCategory subCategory = modelMapper.map(createSubCategoryDto, SubCategory.class);
        subCategoryService.add(subCategory);
    }

    @Override
    public void update(UpdateSubCategoryDto updateSubCategoryDto) {
        SubCategory subCategory = modelMapper.map(updateSubCategoryDto, SubCategory.class);
        subCategoryService.update(subCategory);
    }

    @Override
    public List<SubCategoryListingDto> getAll() {
        List<SubCategory> subCategories = subCategoryService.getAll();
        return subCategories.stream()
                .map(subCategory -> modelMapper.map(subCategory, SubCategoryListingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubCategoryListingDto> getAllByCategoryId(Integer categoryId) {
        List<SubCategory> subCategories = subCategoryService.getAll();
        return subCategories.stream()
                .filter(subCategory -> subCategory.getCategory().getId().equals(categoryId)) // Filter by categoryId
                .map(subCategory -> modelMapper.map(subCategory, SubCategoryListingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SubCategory> findById(Integer id) {
        return subCategoryService.getById(id);
    }

}
