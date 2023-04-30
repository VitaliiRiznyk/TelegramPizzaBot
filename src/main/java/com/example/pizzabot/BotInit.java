package com.example.pizzabot;

import com.example.pizzabot.model.Pizza;
import com.example.pizzabot.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInit {

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            pizzaRepository.save(new Pizza("Гавайська", 220,
                    "https://upload.wikimedia.org/wikipedia/commons/e/ea/Pizza_with_pineapple.jpg"));
            pizzaRepository.save(new Pizza("Італійська", 200,
                    "https://upload.wikimedia.org/wikipedia/commons/c/c8/Pizza_Margherita_stu_spivack.jpg"));
            pizzaRepository.save(new Pizza("М'ясна", 240,
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/NYPizzaPie.jpg/800px-NYPizzaPie.jpg"));
            pizzaRepository.save(new Pizza("Сирна", 190,
                    "https://upload.wikimedia.org/wikipedia/commons/a/ae/Cheese_pizza_with_melted_cheese.jpg"));
        };
    }

    @Autowired
    BotPizza botPizza;
    @Autowired
    private PizzaRepository pizzaRepository;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(botPizza);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
