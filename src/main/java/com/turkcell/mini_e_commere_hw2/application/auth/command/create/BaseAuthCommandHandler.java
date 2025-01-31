package com.turkcell.mini_e_commere_hw2.application.auth.command.create;

import com.turkcell.mini_e_commere_hw2.entity.OperationClaim;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.rules.OperationClaimBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.UserBusinessRules;
import com.turkcell.mini_e_commere_hw2.service.domain.OperationClaimService;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import com.turkcell.mini_e_commere_hw2.util.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class BaseAuthCommandHandler {
    protected final UserBusinessRules userBusinessRules;
    protected final PasswordEncoder passwordEncoder;
    protected final OperationClaimBusinessRules operationClaimBusinessRules;
    protected final JwtService jwtService;
    protected final OperationClaimService operationClaimService;

    protected void addIfNotExistsOperationClaim(String name) {
        try {
            operationClaimBusinessRules.operationClaimMustExist(name);
        } catch (BusinessException e) {
            operationClaimService.addOperationClaim(name);
        }
    }

    protected List<OperationClaim> getOperationClaims(String roleName) {
        List<OperationClaim> claims = new ArrayList<>();
        claims.add(operationClaimService.getOperationClaimByName(roleName));
        return claims;
    }

    protected Map<String, Object> getRoles(User user) {
        Map<String, Object> roles = new HashMap<>();
        roles.put("roles", user.getOperationClaims().stream().map(OperationClaim::getName).toList());
        return roles;
    }

    protected void validateUsername(String username) {
        userBusinessRules.usernameMustNotExist(username);
    }
} 