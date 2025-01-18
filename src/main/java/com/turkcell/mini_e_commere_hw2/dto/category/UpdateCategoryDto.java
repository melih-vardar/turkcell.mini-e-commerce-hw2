package com.turkcell.mini_e_commere_hw2.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCategoryDto {
    private Integer id;
    
    @NotBlank(message = "Name cannot be empty")
    @Length(min = 3, message = "Name must be at least 3 characters")
    private String name;
}
