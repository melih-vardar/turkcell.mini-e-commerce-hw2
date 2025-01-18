package com.turkcell.mini_e_commere_hw2.repository;


import com.turkcell.mini_e_commere_hw2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByUsername(String username);
}
