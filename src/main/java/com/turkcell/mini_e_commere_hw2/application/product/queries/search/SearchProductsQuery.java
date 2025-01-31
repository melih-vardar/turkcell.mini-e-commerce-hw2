package com.turkcell.mini_e_commere_hw2.application.product.queries.search;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class SearchProductsQuery implements Command<List<ProductListingDto>> {
    private String categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean inStock;

    @Component
    public static class Handler implements Command.Handler<SearchProductsQuery, List<ProductListingDto>> {
        private final ProductRepository productRepository;

        public Handler(ProductRepository productRepository) {
            this.productRepository = productRepository;
        }

        @Override
        public List<ProductListingDto> handle(SearchProductsQuery query) {
            List<Product> products = productRepository.search(
                query.getCategoryId(),
                query.getMinPrice(),
                query.getMaxPrice(),
                query.getInStock()
            );

            return products.stream()
                .map(product -> new ProductListingDto(
                    product.getId(),
                    product.getName(),
                    product.getUnitPrice(),
                    product.getStock(),
                    product.getCategory().getId(),
                    product.getCategory().getName(),
                    product.getDescription(),
                    product.getImage()
                ))
                .collect(Collectors.toList());
        }
    }
} 