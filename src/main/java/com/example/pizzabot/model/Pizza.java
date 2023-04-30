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

    private Integer pizzaPrice;

    private String pizzaPicture;

    public Pizza() {
    }

    public Pizza(String pizzaName, Integer pizzaPrice, String pizzaPicture) {
        this.pizzaName = pizzaName;
        this.pizzaPrice = pizzaPrice;
        this.pizzaPicture = pizzaPicture;
    }

}