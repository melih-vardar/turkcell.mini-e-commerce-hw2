package com.turkcell.mini_e_commere_hw2.application.product.commands.update;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.rules.CategoryBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.ProductBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateProductCommand implements Command<Void>
{
    @NotNull(message = "ID cannot be null")
    private Integer id;

    @NotBlank(message = "Name cannot be empty")
    @Length(min = 3, max = 50, message = "Name must be at least 3 characters and at most 50 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than 0")
    private BigDecimal unitPrice;

    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private int stock;

    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;

    @Length(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @Length(max = 255, message = "Image URL must be at most 255 characters")
    private String image;

    @Component
    @RequiredArgsConstructor
    public static class UpdateProductCommandHandler implements Command.Handler<UpdateProductCommand, Void> {
        private final ProductRepository productRepository;
        private final CategoryRepository categoryRepository;
        private final ProductBusinessRules productBusinessRules;
        private final CategoryBusinessRules categoryBusinessRules;


        @Override
        public Void handle(UpdateProductCommand command) {
            // Business Rules
            categoryBusinessRules.categoryMustExist(command.getCategoryId());
            productBusinessRules.productNameMustExist(command.getName());

            // Manual Mapping
            // UpdateProductCommand -> Product
            Product product = productRepository.findById(command.getId()).orElseThrow(() -> new BusinessException("Product not found"));
            product.setName(command.getName());
            product.setUnitPrice(command.getUnitPrice());
            product.setStock(command.getStock());
            product.setCategory(categoryRepository.findById(command.getCategoryId()).orElseThrow(() -> new BusinessException("Category not found")));
            product.setDescription(command.getDescription());
            product.setImage(command.getImage());

            productRepository.save(product);

            return null;
        }
    }
}
