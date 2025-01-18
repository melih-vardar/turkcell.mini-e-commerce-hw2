package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.product.CreateProductDto;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.dto.product.UpdateProductDto;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.rules.SubCategoryBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final SubCategoryService subCategoryService;
    private final SubCategoryBusinessRules subCategoryBusinessRules;

    public ProductServiceImpl(ProductRepository productRepository, SubCategoryService subCategoryService, SubCategoryBusinessRules subCategoryBusinessRules) {
        this.productRepository = productRepository;
        this.subCategoryService = subCategoryService;
        this.subCategoryBusinessRules = subCategoryBusinessRules;
    }

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
        List<ProductListingDto> productListingDtos = productRepository
                .findAll()
                .stream()
                .map((product) -> {
                    ProductListingDto productListingDto = new ProductListingDto(
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
                    return productListingDto;
                })
                .toList();

        return productListingDtos;
    }

    @Override
    public List<ProductListingDto> search(String categoryId, BigDecimal minPrice, BigDecimal maxPrice, boolean inStock) {
        Integer categoryIdInt = categoryId != null && !categoryId.isEmpty() ? Integer.parseInt(categoryId) : null;

        List<Product> products = productRepository.search(categoryIdInt, minPrice, maxPrice, inStock);

        List<ProductListingDto> productListingDtos = products.stream()
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

        return productListingDtos;
    }

    @Override
    public ProductListingDto findById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));

        ProductListingDto productListingDto = new ProductListingDto(
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

        return productListingDto;
    }
}
