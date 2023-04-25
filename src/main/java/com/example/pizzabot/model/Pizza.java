package com.example.pizzabot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String pizzaName;

    private Double pizzaPrice;

    public Pizza() {
    }

    public Pizza(String pizzaName, Double pizzaPrice) {
        this.pizzaName = pizzaName;
        this.pizzaPrice = pizzaPrice;
    }

}