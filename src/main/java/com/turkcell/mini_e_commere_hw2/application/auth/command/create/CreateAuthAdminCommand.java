package com.turkcell.mini_e_commere_hw2.application.auth.command.create;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.Admin;
import com.turkcell.mini_e_commere_hw2.entity.OperationClaim;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.AdminRepository;
import com.turkcell.mini_e_commere_hw2.rules.OperationClaimBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.UserBusinessRules;
import com.turkcell.mini_e_commere_hw2.service.domain.OperationClaimService;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import com.turkcell.mini_e_commere_hw2.util.jwt.JwtService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CreateAuthAdminCommand implements Command<AuthResponse> {
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    private String surname;

    @NotBlank(message = "Username cannot be empty")
    @Email(message = "Invalid email format")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    private String password;

    @Component
    @RequiredArgsConstructor
    public static class CreateAuthCommandHandler implements Command.Handler<CreateAuthAdminCommand, AuthResponse> {
        private final UserBusinessRules userBusinessRules;
        private final PasswordEncoder passwordEncoder;
        private final OperationClaimBusinessRules operationClaimBusinessRules;
        private final JwtService jwtService;
        private final AdminRepository adminRepository;
        private final OperationClaimService operationClaimService;

        @Override
        public AuthResponse handle(CreateAuthAdminCommand command) {
            userBusinessRules.usernameMustNotExist(command.getUsername());

            // mapping
            Admin admin = new Admin();
            admin.setName(command.getName());
            admin.setSurname(command.getSurname());
            admin.setUsername(command.getUsername());
            admin.setPassword(passwordEncoder.encode(command.getPassword()));

            // Set admin role
            addIfNotExistsOperationClaim("admin");

            List<OperationClaim> claims = new ArrayList<>();
            claims.add(operationClaimService.getOperationClaimByName("admin"));
            admin.setOperationClaims(claims);

            adminRepository.save(admin);

            String token = jwtService.generateToken(admin.getUsername(), getRoles(admin));
            return new AuthResponse(token);
        }

        private void addIfNotExistsOperationClaim(String name) {
            try {
                operationClaimBusinessRules.operationClaimMustExist(name);
            } catch (BusinessException e) {
                operationClaimService.addOperationClaim(name);
            }
        }


        private Map<String, Object> getRoles(User user) {
            Map<String, Object> roles = new HashMap<>();
            roles.put("roles", user.getOperationClaims().stream().map(OperationClaim::getName).toList());
            return roles;
        }
    }


}
