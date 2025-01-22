package com.turkcell.mini_e_commere_hw2.dto.user;

import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserListingDto {
    private UUID id;
    private String name;
    private String surname;
    private String username;
}
