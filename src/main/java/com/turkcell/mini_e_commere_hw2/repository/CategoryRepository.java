package com.turkcell.mini_e_commere_hw2.repository;


import com.turkcell.mini_e_commere_hw2.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
