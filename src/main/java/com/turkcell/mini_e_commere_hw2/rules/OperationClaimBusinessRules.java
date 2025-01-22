package com.turkcell.mini_e_commere_hw2.rules;

import com.turkcell.mini_e_commere_hw2.repository.OperationClaimRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OperationClaimBusinessRules {
    private final OperationClaimRepository operationClaimRepository;

    public void operationClaimMustExist(String name) {
        operationClaimRepository.findByName(name).orElseThrow(() -> new BusinessException("Operation claim not found"));
    }
}