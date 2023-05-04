package com.example.pizzabot.service;

import com.example.pizzabot.model.Pizza;
import com.example.pizzabot.model.PizzaOrder;
import com.example.pizzabot.repository.PizzaOrderRepository;
import com.example.pizzabot.repository.PizzaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
public class PizzaOrderService {

    private PizzaOrderRepository pizzaOrderRepository;

    private PizzaService pizzaService;
    private final PizzaRepository pizzaRepository;

    public PizzaOrderService(PizzaOrderRepository pizzaOrderRepository, PizzaService pizzaService,
                             PizzaRepository pizzaRepository) {
        this.pizzaOrderRepository = pizzaOrderRepository;
        this.pizzaService = pizzaService;
        this.pizzaRepository = pizzaRepository;
    }

    public void newPizzaOrder(String pizzas_address, Long chat_id) {
        pizzaOrderRepository.save(new PizzaOrder(null, chat_id, new Date(Calendar.getInstance().getTimeInMillis()),
                pizzas_address, null, false));
    }

    public void updatePizzaOrderByPizza(String pizzaName, Long chatId) {
        Pizza pizza = pizzaService.findByName(pizzaName);
        pizzaOrderRepository.updatePizzaByChatId(pizza, chatId);
    }

    public void updatePizzaOrderIngredient(String pizzaIngredient, Long chatId) {
        if (pizzaIngredient != null) {
            pizzaOrderRepository.updatePizzaIngredientByChatIdAndId(pizzaIngredient, chatId);
        }
    }

    @Transactional(readOnly = true)
    public PizzaOrder getPizzaOrder(Long chatId) {
        if (chatId != null && pizzaOrderRepository.findByChatId(chatId).isPresent()) {
            return pizzaOrderRepository.findByChatId(chatId).get();
        }
        return null;
    }

    public void updatePizzaOrderByPhone(String phoneNumber, Long chatId) {
        pizzaOrderRepository.updateUserPhoneNumberByChatId(phoneNumber, chatId);
    }

    public void updatePizzaOrderWhenPayed(Long chatId) {
        pizzaOrderRepository.updateIsPayedByChatId(chatId);
    }

}