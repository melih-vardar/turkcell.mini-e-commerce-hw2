package com.turkcell.mini_e_commere_hw2.application.auth.command.create;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.Admin;
import com.turkcell.mini_e_commere_hw2.repository.AdminRepository;
import com.turkcell.mini_e_commere_hw2.rules.OperationClaimBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.UserBusinessRules;
import com.turkcell.mini_e_commere_hw2.service.domain.OperationClaimService;
import com.turkcell.mini_e_commere_hw2.util.jwt.JwtService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class CreateAuthAdminCommand extends CreateAuthCommand {
    @Component
    public static class Handler extends BaseAuthCommandHandler implements Command.Handler<CreateAuthAdminCommand, AuthResponse> {
        private final AdminRepository adminRepository;

        public Handler(
                UserBusinessRules userBusinessRules,
                PasswordEncoder passwordEncoder,
                OperationClaimBusinessRules operationClaimBusinessRules,
                JwtService jwtService,
                OperationClaimService operationClaimService,
                AdminRepository adminRepository
        ) {
            super(userBusinessRules, passwordEncoder, operationClaimBusinessRules, jwtService, operationClaimService);
            this.adminRepository = adminRepository;
        }

        @Override
        public AuthResponse handle(CreateAuthAdminCommand command) {
            validateUsername(command.getUsername());

            Admin admin = new Admin();
            admin.setName(command.getName());
            admin.setSurname(command.getSurname());
            admin.setUsername(command.getUsername());
            admin.setPassword(passwordEncoder.encode(command.getPassword()));

            addIfNotExistsOperationClaim("admin");
            admin.setOperationClaims(getOperationClaims("admin"));

            adminRepository.save(admin);

            String token = jwtService.generateToken(admin.getUsername(), getRoles(admin));
            return new AuthResponse(token);
        }
    }
}
