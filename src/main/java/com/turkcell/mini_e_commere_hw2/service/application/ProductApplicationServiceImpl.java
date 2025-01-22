package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.product.CreateProductDto;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.dto.product.UpdateProductDto;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.service.domain.ProductService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductApplicationServiceImpl implements ProductApplicationService {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public void add(CreateProductDto createProductDto) {
        Product product = modelMapper.map(createProductDto, Product.class);
        productService.add(product);
    }

    @Override
    public void update(UpdateProductDto updateProductDto) {
        Product product = modelMapper.map(updateProductDto, Product.class);
        productService.update(product);
    }

    @Override
    public List<ProductListingDto> getAll() {
        List<Product> products = productService.getAll();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductListingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductListingDto> search(
            String categoryId,
            String subCategoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean inStock) {
        List<Product> products = productService.search(categoryId, subCategoryId, minPrice, maxPrice, inStock);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductListingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductListingDto findById(Integer id) {
        Product product = productService.getById(id);
        return modelMapper.map(product, ProductListingDto.class);
    }

    @Override
    public void delete(Integer id) {
        productService.delete(id);
    }
}
