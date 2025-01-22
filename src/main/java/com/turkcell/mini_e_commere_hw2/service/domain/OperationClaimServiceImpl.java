package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.OperationClaim;
import com.turkcell.mini_e_commere_hw2.repository.OperationClaimRepository;
import com.turkcell.mini_e_commere_hw2.rules.OperationClaimBusinessRules;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OperationClaimServiceImpl implements OperationClaimService {
    private final OperationClaimRepository operationClaimRepository;
    private final OperationClaimBusinessRules operationClaimBusinessRules;

    @Override
    public void addOperationClaim(String name) {
        operationClaimRepository.save(new OperationClaim(name));
    }

    @Override
    public void deleteOperationClaim(String name) {
        operationClaimBusinessRules.operationClaimMustExist(name);
        operationClaimRepository.delete(operationClaimRepository.findByName(name).orElseThrow());
    }

    @Override
    public void updateOperationClaim(UUID id, String name) {
        operationClaimBusinessRules.operationClaimMustExist(name);
        OperationClaim operationClaim = operationClaimRepository.findById(id).orElseThrow();
        if (operationClaim != null) {
            operationClaim.setName(name);
            operationClaimRepository.save(operationClaim);
        }
    }

    @Override
    public List<OperationClaim> getAllOperationClaims() {
        return operationClaimRepository.findAll();
    }

    @Override
    public OperationClaim getOperationClaimByName(String name) {
        operationClaimBusinessRules.operationClaimMustExist(name);
        return operationClaimRepository.findByName(name).orElseThrow();
    }
}
