package com.turkcell.mini_e_commere_hw2.repository;

import com.turkcell.mini_e_commere_hw2.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
    boolean existsByParentId(Integer parentId);

    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId")
    List<Category> findAllByParentId(@Param("parentId") Integer parentId);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.subCategories WHERE c.parent IS NULL")
    List<Category> findAllRootCategories();

    @Query("SELECT DISTINCT c FROM Category c " +
           "LEFT JOIN FETCH c.subCategories " +
           "LEFT JOIN FETCH c.products " +
           "WHERE c.id = :id")
    Optional<Category> findByIdWithDetails(@Param("id") Integer id);
}
