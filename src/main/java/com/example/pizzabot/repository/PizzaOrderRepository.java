package com.example.pizzabot.repository;

import com.example.pizzabot.model.PizzaOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PizzaOrderRepository extends JpaRepository<PizzaOrder, Long> {

    @Transactional
    @Modifying
    @Query("update PizzaOrder p set p.pizzaIngredient = ?1 where p.chatId = ?2 and p.id =" +
            "( select max_id from (select max(p.id) as max_id from PizzaOrder p where p.chatId=?2) as subquery)")
    int updatePizzaIngredientByChatIdAndId(String pizzaIngredient, @NonNull Long chatId);


    @Transactional
    @Query("select p from PizzaOrder p where p.chatId = ?1 and p.id=(select max (p.id) from PizzaOrder p where p.chatId = ?1)")
    Optional<PizzaOrder> findByChatId(@NonNull Long chatId);

}