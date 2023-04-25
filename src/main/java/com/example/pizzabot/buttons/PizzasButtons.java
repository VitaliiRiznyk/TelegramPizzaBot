package com.example.pizzabot.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public interface PizzasButtons {

    List<KeyboardButton> keybordPizzaButtons = List.of(new KeyboardButton("Гавайська"), new KeyboardButton("Італійська")
            , new KeyboardButton("М'ясна"), new KeyboardButton("Сирна"));

    ReplyKeyboardMarkup replyPizzaChoice = new ReplyKeyboardMarkup(List.of(new KeyboardRow(keybordPizzaButtons)),
            true, true, true, "Оберіть піццу", true);
}