package com.example.pizzabot.service;

import com.example.pizzabot.model.Pizza;
import com.example.pizzabot.model.PizzaOrder;
import com.example.pizzabot.repository.PizzaOrderRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class PizzaOrderService {

    private PizzaOrderRepository pizzaOrderRepository;

    private PizzaService pizzaService;

    public PizzaOrderService(PizzaOrderRepository pizzaOrderRepository, PizzaService pizzaService) {
        this.pizzaOrderRepository = pizzaOrderRepository;
        this.pizzaService = pizzaService;
    }

    public void newPizzaOrder(String pizzaName, Long chatId) {
        if (pizzaName != null) {
            Pizza pizza = pizzaService.findByName(pizzaName);
            pizzaOrderRepository.save(new PizzaOrder(pizza, chatId,
                    new Date(Calendar.getInstance().getTimeInMillis()), null));
        }
    }

    public void updatePizzaOrderIngredient(String pizzaIngredient, Long chatId) {
        if (pizzaIngredient != null) {
            pizzaOrderRepository.updatePizzaIngredientByChatIdAndId(pizzaIngredient, chatId);
        }
    }

    public PizzaOrder getPizzaOrder(Long chatId) {
        if (chatId != null && pizzaOrderRepository.findByChatId(chatId).isPresent()) {
            return pizzaOrderRepository.findByChatId(chatId).get();
        }
        return null;
    }

}