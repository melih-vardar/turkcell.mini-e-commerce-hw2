package com.turkcell.mini_e_commere_hw2.repository;

import com.turkcell.mini_e_commere_hw2.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer>
{
  Optional<Product> findByName(String name);
  Optional<Product> findByNameIsAndIdIsNot(String name, Integer id);

  @Query("SELECT p FROM Product p " +
         "WHERE (:categoryId IS NULL OR p.subCategory.category.id = :categoryId) " +
         "AND (:minPrice IS NULL OR p.unitPrice >= :minPrice) " +
         "AND (:maxPrice IS NULL OR p.unitPrice <= :maxPrice) " +
         "AND (:inStock = false OR p.stock > 0)")
  List<Product> search(
          @Param("categoryId") Integer categoryId,
          @Param("minPrice") BigDecimal minPrice,
          @Param("maxPrice") BigDecimal maxPrice,
          @Param("inStock") boolean inStock
  );
}
