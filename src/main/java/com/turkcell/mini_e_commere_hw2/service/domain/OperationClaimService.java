package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.OperationClaim;

import java.util.List;
import java.util.UUID;

public interface OperationClaimService {
    void addOperationClaim(String name);
    void deleteOperationClaim(String name);
    void updateOperationClaim(UUID id, String name);
    List<OperationClaim> getAllOperationClaims();
    OperationClaim getOperationClaimByName(String name);
}
