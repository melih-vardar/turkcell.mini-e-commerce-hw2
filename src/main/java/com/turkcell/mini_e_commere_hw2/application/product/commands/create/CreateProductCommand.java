package com.turkcell.mini_e_commere_hw2.application.product.commands.create;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.Category;
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
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductCommand implements Command<CreatedProductResponse> {
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
    public static class Handler implements Command.Handler<CreateProductCommand, CreatedProductResponse> {
        private final ProductRepository productRepository;
        private final CategoryRepository categoryRepository;
        private final ProductBusinessRules productBusinessRules;
        private final CategoryBusinessRules categoryBusinessRules;

        public Handler(
                ProductRepository productRepository,
                CategoryRepository categoryRepository,
                ProductBusinessRules productBusinessRules,
                CategoryBusinessRules categoryBusinessRules) {
            this.productRepository = productRepository;
            this.categoryRepository = categoryRepository;
            this.productBusinessRules = productBusinessRules;
            this.categoryBusinessRules = categoryBusinessRules;
        }

        @Override
        public CreatedProductResponse handle(CreateProductCommand command) {
            // Business Rules
            categoryBusinessRules.categoryMustExist(command.getCategoryId());
            productBusinessRules.productNameMustExist(command.getName());

            // Get Category
            Category category = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(() -> new BusinessException("Category not found"));

            // Create product
            Product product = new Product();
            product.setName(command.getName());
            product.setUnitPrice(command.getUnitPrice());
            product.setStock(command.getStock());
            product.setCategory(category);
            product.setDescription(command.getDescription());
            product.setImage(command.getImage());

            productRepository.save(product);

            return new CreatedProductResponse(product.getId());
        }
    }
} 