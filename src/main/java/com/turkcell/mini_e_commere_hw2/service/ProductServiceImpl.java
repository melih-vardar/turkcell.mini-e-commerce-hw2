package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.product.CreateProductDto;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.dto.product.UpdateProductDto;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.rules.SubCategoryBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final SubCategoryService subCategoryService;
    private final SubCategoryBusinessRules subCategoryBusinessRules;


    @Override
    public void add(CreateProductDto createProductDto) {
        subCategoryBusinessRules.subCategoryMustExist(createProductDto.getSubCategoryId());

        SubCategory subCategory = subCategoryService
                .findById(createProductDto.getSubCategoryId())
                .orElse(null);

        Product productWithSameName = productRepository
                .findByName(createProductDto.getName())
                .orElse(null);

        if (productWithSameName != null)
            throw new BusinessException("Product already exists");

        Product product = new Product();
        product.setName(createProductDto.getName());
        product.setStock(createProductDto.getStock());
        product.setUnitPrice(createProductDto.getUnitPrice());
        product.setSubCategory(subCategory);
        product.setDescription(createProductDto.getDescription());
        product.setImage(createProductDto.getImage());

        productRepository.save(product);
    }

    @Override
    public void update(UpdateProductDto updateProductDto) {
        subCategoryBusinessRules.subCategoryMustExist(updateProductDto.getSubCategoryId());


        SubCategory subCategory = subCategoryService
                .findById(updateProductDto.getSubCategoryId())
                .orElse(null);

        Product productWithSameName = productRepository
                .findByNameIsAndIdIsNot(updateProductDto.getName(), updateProductDto.getId())
                .orElse(null);

        if (productWithSameName != null)
            throw new BusinessException("Product already exists");

        Product productToUpdate = productRepository.findById(updateProductDto.getId())
                .orElseThrow(() -> new BusinessException("Product not found"));

        productToUpdate.setName(updateProductDto.getName());
        productToUpdate.setStock(updateProductDto.getStock());
        productToUpdate.setUnitPrice(updateProductDto.getUnitPrice());
        productToUpdate.setSubCategory(subCategory);
        productToUpdate.setDescription(updateProductDto.getDescription());
        productToUpdate.setImage(updateProductDto.getImage());

        productRepository.save(productToUpdate);
    }

    @Override
    public List<ProductListingDto> getAll() {

        return productRepository
                .findAll()
                .stream()
                .map((product) -> {
                    return new ProductListingDto(
                            product.getId(),
                            product.getName(),
                            product.getUnitPrice(),
                            product.getStock(),
                            product.getSubCategory().getId(),
                            product.getSubCategory().getName(),
                            product.getSubCategory().getCategory().getId(),
                            product.getSubCategory().getCategory().getName(),
                            product.getDescription(),
                            product.getImage()
                    );
                })
                .toList();
    }

    @Override
    public List<ProductListingDto> search(String categoryId, String subCategoryId, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock) {
        List<Product> products = productRepository.search(categoryId, subCategoryId, minPrice, maxPrice, inStock);

        return products.stream()
                .map(product -> new ProductListingDto(
                                product.getId(),
                                product.getName(),
                                product.getUnitPrice(),
                                product.getStock(),
                                product.getSubCategory().getId(),
                                product.getSubCategory().getName(),
                                product.getSubCategory().getCategory().getId(),
                                product.getSubCategory().getCategory().getName(),
                                product.getDescription(),
                                product.getImage()
                        )
                )
                .toList();
    }

    @Override
    public ProductListingDto findById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));

        return new ProductListingDto(
                product.getId(),
                product.getName(),
                product.getUnitPrice(),
                product.getStock(),
                product.getSubCategory().getId(),
                product.getSubCategory().getName(),
                product.getSubCategory().getCategory().getId(),
                product.getSubCategory().getCategory().getName(),
                product.getDescription(),
                product.getImage()
        );
    }

    @Override
    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));

        productRepository.delete(product);
    }
}
