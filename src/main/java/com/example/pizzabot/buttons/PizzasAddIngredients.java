package com.example.pizzabot.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public interface PizzasAddIngredients {

    List<KeyboardButton> keyboardReplyIngredients = List.of(new KeyboardButton("Моцарелла"),
            new KeyboardButton("Огірочки"), new KeyboardButton("Ананас"),
            new KeyboardButton("Без інгредієнту"));

    ReplyKeyboardMarkup ingredientsKeyboardMarkup = new ReplyKeyboardMarkup(List.of(new KeyboardRow(keyboardReplyIngredients)),
            true, true, true, "Введіть інгредієнти", true);

}