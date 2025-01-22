package com.turkcell.mini_e_commere_hw2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="admins")
@AllArgsConstructor
@Getter
@Setter
public class Admin extends User{

}