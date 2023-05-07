package com.example.pizzabot.repository;

import com.example.pizzabot.model.PizzaOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PizzaOrderRepository extends JpaRepository<PizzaOrder, Long> {
    @Query("select p from PizzaOrder p where p.id = ?1 and p.chatId = ?2")
    PizzaOrder findByIdAndChatId(@NonNull Long id, @NonNull Long chatId);


    Optional<Object> findByChatId(Long chatId);

}