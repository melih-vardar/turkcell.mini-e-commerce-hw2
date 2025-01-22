package com.turkcell.mini_e_commere_hw2.repository;

import com.turkcell.mini_e_commere_hw2.entity.OperationClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OperationClaimRepository extends JpaRepository<OperationClaim, UUID> {
    Optional<OperationClaim> findByName(String name);
}
