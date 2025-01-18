package com.turkcell.mini_e_commere_hw2.dto.subcategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;


public class CreateSubCategoryDto {
    @NotBlank(message = "Name cannot be empty")
    @Length(min = 3, message = "Name must be at least 3 characters")
    private String name;

    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;

    public CreateSubCategoryDto() {
    }

    public CreateSubCategoryDto(String name, Integer categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}