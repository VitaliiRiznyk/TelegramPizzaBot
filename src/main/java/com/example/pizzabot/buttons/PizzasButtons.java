package com.example.pizzabot.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public interface PizzasButtons {
    //  місце розташування вибір
    List<KeyboardButton> keyboardPizzaAddresses = List.of(new KeyboardButton("Пітерська 5"),
            new KeyboardButton("Кирилівська 9"), new KeyboardButton("Гната Юри 4"));

    ReplyKeyboardMarkup replyAddresses = new ReplyKeyboardMarkup(List.of(new KeyboardRow(keyboardPizzaAddresses)), true, false,
            true, "Зробіть вибір", true);
    //  піцца вибір
    List<KeyboardButton> keyboardPizzaButtons = List.of(new KeyboardButton("Гавайська"),
            new KeyboardButton("Італійська"), new KeyboardButton("М'ясна"), new KeyboardButton("Сирна"));

    ReplyKeyboardMarkup replyPizzaChoice = new ReplyKeyboardMarkup(List.of(new KeyboardRow(keyboardPizzaButtons)),
            true, false, true, "Оберіть піццу", true);
    //  вибір додаткових матеріалів
    List<KeyboardButton> keyboardReplyIngredients = List.of(new KeyboardButton("Моцарелла"),
            new KeyboardButton("Огірочки"), new KeyboardButton("Ананас"),
            new KeyboardButton("Без інгредієнту"));

    ReplyKeyboardMarkup ingredientsKeyboardMarkup = new ReplyKeyboardMarkup(List.of(new KeyboardRow(keyboardReplyIngredients)),
            true, true, true, "Введіть інгредієнти", false);
}