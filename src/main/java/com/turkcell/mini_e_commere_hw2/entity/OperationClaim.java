package com.turkcell.mini_e_commere_hw2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "operation_claims")
@Getter
@Setter
@NoArgsConstructor
public class OperationClaim {
    @Id
    @UuidGenerator
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "operationClaims")
    private List<User> users;

    public OperationClaim(String name) {
        this.name = name;
    }
}