package com.turkcell.mini_e_commere_hw2.application.product.queries.get;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetListProductQuery implements Command<List<ProductListingDto>> {

    @Component
    @RequiredArgsConstructor
    public static class GetListProductQueryHandler
            implements Command.Handler<GetListProductQuery, List<ProductListingDto>> {

        private final ProductRepository productRepository;

        public List<ProductListingDto> handle(GetListProductQuery getListProductQuery) {
            List<Product> products = productRepository.findAll();

            List<ProductListingDto> productDtos = products
                    .stream()
                    .map(product -> new ProductListingDto
                            (
                                    product.getId(),
                                    product.getName(),
                                    product.getUnitPrice(),
                                    product.getStock(),
                                    product.getCategory().getId(),
                                    product.getCategory().getName(),
                                    product.getDescription(),
                                    product.getImage()))
                    .toList();

            return productDtos;
        }
    }
}
