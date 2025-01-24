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
  boolean existsByCategoryId(Integer categoryId);

  @Query("SELECT DISTINCT p FROM Product p " +
         "JOIN p.category c " +
         "LEFT JOIN c.parent parent " +
         "WHERE (:categoryId IS NULL OR c.id = :categoryId OR parent.id = :categoryId) " +
         "AND (:minPrice IS NULL OR p.unitPrice >= :minPrice) " +
         "AND (:maxPrice IS NULL OR p.unitPrice <= :maxPrice) " +
         "AND (:inStock IS NULL OR (:inStock = true AND p.stock > 0) OR (:inStock = false AND p.stock = 0))")
  List<Product> search(
          @Param("categoryId") String categoryId,
          @Param("minPrice") BigDecimal minPrice,
          @Param("maxPrice") BigDecimal maxPrice,
          @Param("inStock") Boolean inStock
  );
}
