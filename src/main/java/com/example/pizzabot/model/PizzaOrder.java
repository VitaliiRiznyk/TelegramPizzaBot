package com.example.pizzabot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@ToString
public class PizzaOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Pizza pizza;

    private Long chatId;

    private String pizzasAddress;

    private String pizzaIngredient;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    private Date orderTime;

    public PizzaOrder() {
    }

    public PizzaOrder(Pizza pizza, Long chatId, Date orderTime, String pizzasAddress, String pizzaIngredient) {
        this.pizza = pizza;
        this.chatId = chatId;
        this.orderTime = orderTime;
        this.pizzasAddress = pizzasAddress;
        this.pizzaIngredient = pizzaIngredient;
    }

}