package com.example.pizzabot.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public interface PizzasAddress {


    List<KeyboardButton> keyboardPizzaAddresses = List.of(new KeyboardButton("Пітерська 5"),
            new KeyboardButton("Кирилівська 9"), new KeyboardButton("Гната Юри 4"));

    ReplyKeyboardMarkup replyAddresses = new ReplyKeyboardMarkup(List.of(new KeyboardRow(keyboardPizzaAddresses)), true, true,
            true, "Зробіть вибір", true);

}