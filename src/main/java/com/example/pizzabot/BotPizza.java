package com.example.pizzabot;

import com.example.pizzabot.buttons.PizzasAddIngredients;
import com.example.pizzabot.buttons.PizzasButtons;
import com.example.pizzabot.service.PizzaOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
public class BotPizza extends TelegramLongPollingBot implements PizzasButtons, PizzasAddIngredients {

    private final PizzaOrderService pizzaOrderService;

    @Override
    public String getBotUsername() {
        return "ePizzaBot";
    }

    @Override
    public String getBotToken() {
        return "6107355843:AAES7br4y5YSUlAiLw5yDftgZQNgeF7MLik";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if (message_text.equals("/start")) {
                initialMessage(chat_id);
            } else if (BotPizza.keybordPizzaButtons.contains(new KeyboardButton(message_text))) {
                createNewPizzaOrder(message_text, chat_id);
            } else if (BotPizza.keybordReplyIngredients.contains(new KeyboardButton(message_text))) {
                updatePizzaOrder(message_text, chat_id);
            }

        }
    }

    private void initialMessage(Long chat_id) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Оберіть піццу");
        message.setReplyMarkup(BotPizza.replyPizzaChoice);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void createNewPizzaOrder(String message_text, Long chat_id) {
        pizzaOrderService.newPizzaOrder(message_text, chat_id);
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Ви обрали " + message_text.toLowerCase() + " піццу, оберіть додатковий інгредієнт");
        message.setReplyMarkup(BotPizza.ingredientsKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void updatePizzaOrder(String message_text, Long chat_id) {
        pizzaOrderService.updatePizzaOrderIngredient(message_text.equals("Без інгредієнту") ?
                " без доп. інгредієнтів" : message_text, chat_id);
        SendMessage message = new SendMessage();

        SendPhoto photo = new SendPhoto();
        InputFile file = new InputFile("https://www.foodandwine.com/thmb/7A7CYdDEKJUUhNcLSrlZ5N8wbHo=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/mozzarella-pizza-margherita-FT-RECIPE0621-11fa41ceb1a5465d9036a23da87dd3d4.jpg");
        photo.setPhoto(file);
        photo.setChatId(chat_id);

        String pizzaName = pizzaOrderService.getPizzaOrder(chat_id).getPizza().getPizzaName();
        String pizzaIngredient = pizzaOrderService.getPizzaOrder(chat_id).getPizzaIngredient();
        Double pizzaPrice = pizzaOrderService.getPizzaOrder(chat_id).getPizza().getPizzaPrice();
        message.setChatId(chat_id);
        message.setText("Ваш вибір піцца " + pizzaName + " та інгредієнт: " +
                pizzaIngredient + ". До сплати " + pizzaPrice + " грн.");

        try {
            execute(message);
            execute(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}