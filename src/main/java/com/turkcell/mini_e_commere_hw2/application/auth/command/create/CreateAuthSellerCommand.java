package com.turkcell.mini_e_commere_hw2.application.auth.command.create;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.*;
import com.turkcell.mini_e_commere_hw2.repository.SellerRepository;
import com.turkcell.mini_e_commere_hw2.rules.OperationClaimBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.UserBusinessRules;
import com.turkcell.mini_e_commere_hw2.service.domain.OperationClaimService;
import com.turkcell.mini_e_commere_hw2.service.domain.SellerService;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import com.turkcell.mini_e_commere_hw2.util.jwt.JwtService;
import jakarta.validation.constraints.*;
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
public class CreateAuthSellerCommand implements Command<AuthResponse> {

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

    @NotNull(message = "Company name cannot be null")
    private String companyName;

    @Component
    @RequiredArgsConstructor
    public static class CreateAuthSellerCommandHandler implements Command.Handler<CreateAuthSellerCommand, AuthResponse>{
        private final UserBusinessRules userBusinessRules;
        private final PasswordEncoder passwordEncoder;
        private final OperationClaimBusinessRules operationClaimBusinessRules;
        private final JwtService jwtService;
        private final SellerRepository sellerRepository;
        private final OperationClaimService operationClaimService;

        @Override
        public AuthResponse handle(CreateAuthSellerCommand command) {
            userBusinessRules.usernameMustNotExist(command.getUsername());

            Seller seller = new Seller();
            seller.setName(command.getName());
            seller.setSurname(command.getSurname());
            seller.setUsername(command.getUsername());
            seller.setPassword(passwordEncoder.encode(command.getPassword()));
            seller.setCompanyName(command.getCompanyName());


            addIfNotExistsOperationClaim("seller");

            List<OperationClaim> claims = new ArrayList<>();
            claims.add(operationClaimService.getOperationClaimByName("seller"));
            seller.setOperationClaims(claims);

            sellerRepository.save(seller);

            String token = jwtService.generateToken(seller.getUsername(), getRoles(seller));

            return new AuthResponse(token);
        }

        private void addIfNotExistsOperationClaim(String name){
            try{
                operationClaimBusinessRules.operationClaimMustExist(name);
            } catch (BusinessException e){
                operationClaimService.addOperationClaim(name);
            }
        }

        private Map<String,Object> getRoles(User user){
            Map<String,Object> roles = new HashMap<>();
            roles.put("roles", user.getOperationClaims().stream().map(OperationClaim::getName).toList());
            return roles;
        }
        }
    }

