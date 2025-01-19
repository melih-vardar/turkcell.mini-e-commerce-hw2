package com.turkcell.mini_e_commere_hw2.rules;

import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class UserBusinessRules {
    private final UserRepository userRepository;

    public UserBusinessRules(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void usernameMustNotExist(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new BusinessException("Username already exist");
        });
    }

    public void passwordMustBeValid(String password) {
        if (password.length() < 8) {
            throw new BusinessException("Password must be at least 8 characters");
        }

        if (!password.matches(".*[0-9].*")) {
            throw new BusinessException("Password must contain at least one digit");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new BusinessException("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new BusinessException("Password must contain at least one uppercase letter");
        }
    }
}
