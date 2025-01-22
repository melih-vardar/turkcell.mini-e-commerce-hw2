package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.subcategory.CreateSubCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.SubCategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.UpdateSubCategoryDto;
import com.turkcell.mini_e_commere_hw2.service.application.SubCategoryApplicationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sub-categories")
public class SubCategoryController {
    private final SubCategoryApplicationService subCategoryApplicationService;

    public SubCategoryController(SubCategoryApplicationService subCategoryApplicationService) {
        this.subCategoryApplicationService = subCategoryApplicationService;
    }

    @PostMapping
    public void add(@RequestBody @Valid CreateSubCategoryDto createSubCategoryDto) {
        subCategoryApplicationService.add(createSubCategoryDto);
    }

    @PutMapping
    public void update(@RequestBody @Valid UpdateSubCategoryDto updateSubCategoryDto) {
        subCategoryApplicationService.update(updateSubCategoryDto);
    }

    @GetMapping
    public List<SubCategoryListingDto> getAll() {
        return subCategoryApplicationService.getAll();
    }

    @GetMapping("/by-category/{categoryId}")
    public List<SubCategoryListingDto> getAllByCategoryId(@PathVariable Integer categoryId) {
        return subCategoryApplicationService.getAllByCategoryId(categoryId);
    }
} 