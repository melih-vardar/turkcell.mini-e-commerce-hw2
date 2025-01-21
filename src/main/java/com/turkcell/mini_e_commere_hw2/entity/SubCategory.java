package com.turkcell.mini_e_commere_hw2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sub_categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
}