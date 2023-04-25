package com.example.pizzabot.repository;

import com.example.pizzabot.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    boolean existsByPizzaName(String pizzaName);
    Pizza findByPizzaName(@NonNull String pizzaName);
}
