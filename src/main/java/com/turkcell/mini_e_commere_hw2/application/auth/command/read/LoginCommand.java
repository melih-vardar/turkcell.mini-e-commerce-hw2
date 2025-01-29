package com.turkcell.mini_e_commere_hw2.application.auth.command.read;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.application.auth.command.create.AuthResponse;
import com.turkcell.mini_e_commere_hw2.entity.OperationClaim;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import com.turkcell.mini_e_commere_hw2.util.jwt.JwtService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class LoginCommand implements Command<AuthResponse> {
    @NotBlank(message = "Username cannot be empty")
    @Email(message = "Invalid email format")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @Component
    @RequiredArgsConstructor
    public static class LoginCommandHandler implements Command.Handler<LoginCommand, AuthResponse> {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;

        @Override
        public AuthResponse handle(LoginCommand command) {
            Optional<User> dbUser = userRepository.findByUsername(command.getUsername());

            boolean isPasswordCorrect = dbUser
                    .map(user -> passwordEncoder.matches(command.getPassword(), user.getPassword()))
                    .orElse(false);

            if(!isPasswordCorrect) {
                throw new BusinessException("Invalid or wrong credentials");
            }

            String token = dbUser.map(user-> jwtService.generateToken(user.getUsername(), getRoles(user))).orElseThrow(() -> new BusinessException("User not found"));
            return new AuthResponse(token);
        }

        private Map<String, Object> getRoles(User user) {
            Map<String, Object> roles = new HashMap<>();
            roles.put("roles", user.getOperationClaims().stream().map(OperationClaim::getName).toList());
            return roles;
        }
    }


}
