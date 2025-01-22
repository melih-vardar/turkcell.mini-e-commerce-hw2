package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.Product;
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
    private final SubCategoryBusinessRules subCategoryBusinessRules;

    @Override
    public void add(Product product) {
        subCategoryBusinessRules.subCategoryMustExist(product.getSubCategory().getId());
        Product productWithSameName = productRepository
                .findByName(product.getName())
                .orElse(null);

        if (productWithSameName != null)
            throw new BusinessException("Product already exists");

        productRepository.save(product);
    }

    @Override
    public void update(Product product) {
        subCategoryBusinessRules.subCategoryMustExist(product.getSubCategory().getId());

        Product productWithSameName = productRepository
                .findByNameIsAndIdIsNot(product.getName(), product.getId())
                .orElse(null);

        if (productWithSameName != null)
            throw new BusinessException("Product already exists");

        Product productToUpdate = productRepository.findById(product.getId())
                .orElseThrow(() -> new BusinessException("Product not found"));

        productRepository.save(productToUpdate);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> search(String categoryId, String subCategoryId, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock) {
        return productRepository.search(categoryId, subCategoryId, minPrice, maxPrice, inStock);
    }

    @Override
    public Product getById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));
    }

    @Override
    public Product getByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new BusinessException("Product not found"));
    }

    @Override
    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));

        productRepository.delete(product);
    }
}
