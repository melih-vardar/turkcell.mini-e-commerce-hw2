package com.turkcell.mini_e_commere_hw2.repository;

import com.turkcell.mini_e_commere_hw2.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
