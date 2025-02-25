package com.turkcell.mini_e_commere_hw2.application.auth.command.create;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.Customer;
import com.turkcell.mini_e_commere_hw2.entity.OperationClaim;
import com.turkcell.mini_e_commere_hw2.repository.CustomerRepository;
import com.turkcell.mini_e_commere_hw2.repository.OperationClaimRepository;
import com.turkcell.mini_e_commere_hw2.rules.OperationClaimBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.UserBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.jwt.JwtService;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateAuthCustomerCommand extends CreateAuthCommand {
    @NotNull(message = "Address cannot be null")
    private String address;

    @Component
    public static class Handler extends BaseAuthCommandHandler implements Command.Handler<CreateAuthCustomerCommand, AuthResponse> {
        private final CustomerRepository customerRepository;

        public Handler(
                UserBusinessRules userBusinessRules,
                PasswordEncoder passwordEncoder,
                OperationClaimBusinessRules operationClaimBusinessRules,
                JwtService jwtService,
                OperationClaimRepository operationClaimRepository,
                CustomerRepository customerRepository
        ) {
            super(userBusinessRules, passwordEncoder, operationClaimBusinessRules, jwtService, operationClaimRepository);
            this.customerRepository = customerRepository;
        }

        @Override
        public AuthResponse handle(CreateAuthCustomerCommand command) {
            validateUsername(command.getUsername());

            Customer customer = new Customer();
            customer.setName(command.getName());
            customer.setSurname(command.getSurname());
            customer.setUsername(command.getUsername());
            customer.setPassword(passwordEncoder.encode(command.getPassword()));
            customer.setAddress(command.getAddress());

            Cart cart = new Cart();
            cart.setUser(customer);
            cart.setTotalPrice(BigDecimal.ZERO);
            customer.setCart(cart);

            addIfNotExistsOperationClaim("customer");

            List<OperationClaim> claims = new ArrayList<>();
            claims.add(getOperationClaimByName("customer"));
            customer.setOperationClaims(claims);

            customerRepository.save(customer);

            String token = jwtService.generateToken(customer.getUsername(), getRoles(customer));
            return new AuthResponse(token);
        }
    }
}
