package com.turkcell.mini_e_commere_hw2.application.auth.command.create;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.OperationClaim;
import com.turkcell.mini_e_commere_hw2.entity.Seller;
import com.turkcell.mini_e_commere_hw2.repository.OperationClaimRepository;
import com.turkcell.mini_e_commere_hw2.repository.SellerRepository;
import com.turkcell.mini_e_commere_hw2.rules.OperationClaimBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.UserBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.jwt.JwtService;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateAuthSellerCommand extends CreateAuthCommand {
    @NotNull(message = "Company name cannot be null")
    private String companyName;

    @Component
    public static class Handler extends BaseAuthCommandHandler implements Command.Handler<CreateAuthSellerCommand, AuthResponse> {
        private final SellerRepository sellerRepository;

        public Handler(
                UserBusinessRules userBusinessRules,
                PasswordEncoder passwordEncoder,
                OperationClaimBusinessRules operationClaimBusinessRules,
                JwtService jwtService,
                OperationClaimRepository operationClaimRepository,
                SellerRepository sellerRepository
        ) {
            super(userBusinessRules, passwordEncoder, operationClaimBusinessRules, jwtService, operationClaimRepository);
            this.sellerRepository = sellerRepository;
        }

        @Override
        public AuthResponse handle(CreateAuthSellerCommand command) {
            validateUsername(command.getUsername());

            Seller seller = new Seller();
            seller.setName(command.getName());
            seller.setSurname(command.getSurname());
            seller.setUsername(command.getUsername());
            seller.setPassword(passwordEncoder.encode(command.getPassword()));
            seller.setCompanyName(command.getCompanyName());

            addIfNotExistsOperationClaim("seller");

            List<OperationClaim> claims = new ArrayList<>();
            claims.add(getOperationClaimByName("seller"));
            seller.setOperationClaims(claims);

            sellerRepository.save(seller);

            String token = jwtService.generateToken(seller.getUsername(), getRoles(seller));
            return new AuthResponse(token);
        }
    }
}

