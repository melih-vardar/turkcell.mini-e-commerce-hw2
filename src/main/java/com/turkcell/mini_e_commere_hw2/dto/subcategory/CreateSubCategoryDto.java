package com.turkcell.mini_e_commere_hw2.dto.subcategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubCategoryDto {
    @NotBlank(message = "Name cannot be empty")
    @Length(min = 3, message = "Name must be at least 3 characters")
    private String name;

    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;
}