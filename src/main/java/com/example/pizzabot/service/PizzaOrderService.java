package com.example.pizzabot.service;

import com.example.pizzabot.model.PizzaOrder;
import com.example.pizzabot.repository.PizzaOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PizzaOrderService {

    private final PizzaOrderRepository pizzaOrderRepository;

    public PizzaOrderService(PizzaOrderRepository pizzaOrderRepository) {
        this.pizzaOrderRepository = pizzaOrderRepository;

    }

    public void newPizzaOrder(PizzaOrder pizzaOrder) {
        pizzaOrderRepository.save(pizzaOrder);
    }

    @Transactional(readOnly = true)
    public PizzaOrder getLastPizzaOrder(Long id, Long chatId) {
        if (id != null && chatId != null) {
            return pizzaOrderRepository.findByIdAndChatId(id, chatId);
        }
        return null;
    }

}