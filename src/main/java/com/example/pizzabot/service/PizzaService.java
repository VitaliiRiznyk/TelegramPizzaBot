package com.example.pizzabot.service;

import com.example.pizzabot.model.Pizza;
import com.example.pizzabot.repository.PizzaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PizzaService {

    private PizzaRepository pizzaRepository;

    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @Transactional(readOnly = true)
    public Pizza findByName(String pizzaName) {
        if (pizzaName != null && pizzaRepository.existsByPizzaName(pizzaName)) {
            return pizzaRepository.findByPizzaName(pizzaName);
        }
        return null;
    }

}