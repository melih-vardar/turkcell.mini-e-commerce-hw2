package com.turkcell.mini_e_commere_hw2.application.user.commands.update;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class UpdateUserCommand implements Command<Void> {
    @NotNull(message = "ID cannot be null")
    private UUID id;
    private String name;
    private String surname;
    private String password;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<UpdateUserCommand, Void> {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        private static final Pattern PASSWORD_PATTERN = Pattern.compile(
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"
        );

        @Override
        public Void handle(UpdateUserCommand command) {
            User user = userRepository.findById(command.getId())
                .orElseThrow(() -> new BusinessException("User not found"));

            if(command.getName() != null){
                user.setName(command.getName());
            }
            if(command.getSurname() != null){
                user.setSurname(command.getSurname());
            }

            if (command.getPassword() != null) {
                if (!PASSWORD_PATTERN.matcher(command.getPassword()).matches()) {
                    throw new IllegalArgumentException(
                            "Password must be at least 8 characters long, " +
                                    "contain at least one digit, " +
                                    "one lowercase letter, " +
                                    "and one uppercase letter");
                }
                user.setPassword(passwordEncoder.encode(command.getPassword()));
            }

            userRepository.save(user);
            return null;
        }
    }
} 