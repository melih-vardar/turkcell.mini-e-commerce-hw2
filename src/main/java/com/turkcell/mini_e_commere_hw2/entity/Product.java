package com.turkcell.mini_e_commere_hw2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="unit_price")
    private BigDecimal unitPrice;

    @Column(name="stock")
    private int stock;

    @ManyToOne
    @JoinColumn(name="sub_category_id")
    private SubCategory subCategory;

    @Column(name="description")
    private String description;

    @Column(name="image")
    private String image;
}
