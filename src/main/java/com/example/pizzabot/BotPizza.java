package com.example.pizzabot;

import com.example.pizzabot.buttons.PizzasAddIngredients;
import com.example.pizzabot.buttons.PizzasAddress;
import com.example.pizzabot.buttons.PizzasButtons;
import com.example.pizzabot.mail_sender.EmailSender;
import com.example.pizzabot.model.PizzaOrder;
import com.example.pizzabot.payments.SendPayments;
import com.example.pizzabot.service.PizzaOrderService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
public class BotPizza extends TelegramLongPollingBot implements PizzasButtons,
        PizzasAddIngredients, PizzasAddress, SendPayments {

    private static final Logger LOGGER = LogManager.getLogger(BotPizza.class);

    private final PizzaOrderService pizzaOrderService;

    private final EmailSender mailSender;

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

        if (update.hasPreCheckoutQuery()) {
            AnswerPreCheckoutQuery answerPreCheckoutQuery = new
                    AnswerPreCheckoutQuery(update.getPreCheckoutQuery().getId(), true);

            try {
                execute(answerPreCheckoutQuery);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (update.getMessage().hasSuccessfulPayment()) {
            Integer orderAmount = update.getMessage().getSuccessfulPayment().getTotalAmount();
            long chat_id = update.getMessage().getChatId();
            pizzaOrderService.updatePizzaOrderWhenPayed(chat_id);
            PizzaOrder pizzaOrder = pizzaOrderService.getPizzaOrder(chat_id);
            mailSender.sendMessage("Нове замовлення " + pizzaOrder.getPizza().getPizzaName() + " та інгредієнт " + pizzaOrder.getPizzaIngredient() +
                    " на відділення " + pizzaOrder.getPizzasAddress() + " на оплачену суму " + pizzaOrder.getPizza().getPizzaPrice());
        }

        if (update.hasMessage() && update.getMessage().hasText()) {

            SendMessage message;

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if (message_text.equals("/start") && !update.hasPreCheckoutQuery()) {
                message = initialMessage(chat_id); // вибір найближчої піцерії
                sendMessage(message);
            } else if (BotPizza.keyboardPizzaAddresses.contains(new KeyboardButton(message_text))) {
                message = createOrderByAddress(message_text, chat_id); // вибір піцци
                sendMessage(message);
            } else if (BotPizza.keyboardPizzaButtons.contains(new KeyboardButton(message_text))) {
                message = updateOrderByPizza(message_text, chat_id); // запис вибору піцци
                sendMessage(message);
            } else if (BotPizza.keyboardReplyIngredients.contains(new KeyboardButton(message_text))) {
                updateOrderByIngredient(message_text, chat_id); // фінальне повідомлення про вибір користувача
            }
        }
    }

    private void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage initialMessage(Long chat_id) {
        SendMessage message = new SendMessage(String.valueOf(chat_id), "Оберіть піццерію");
        message.setReplyMarkup(BotPizza.replyAddresses);
        return message;
    }

    private SendMessage createOrderByAddress(String pizzas_address, Long chat_id) {
        pizzaOrderService.newPizzaOrder(pizzas_address, chat_id);
        SendMessage message = new SendMessage(String.valueOf(chat_id), "Оберіть піццу");
        message.setReplyMarkup(BotPizza.replyPizzaChoice);
        return message;
    }

    private SendMessage updateOrderByPizza(String message_text, Long chat_id) {
        pizzaOrderService.updatePizzaOrderByPizza(message_text, chat_id);
        String replyText = "Ви обрали " + message_text.toLowerCase() + " піццу, оберіть додатковий інгредієнт";
        SendMessage message = new SendMessage(String.valueOf(chat_id), replyText);
        message.setReplyMarkup(BotPizza.ingredientsKeyboardMarkup);
        return message;
    }

    private void updateOrderByIngredient(String message_text, Long chat_id) {
        pizzaOrderService.updatePizzaOrderIngredient(message_text.equals("Без інгредієнту") ? " без доп. інгредієнтів" : message_text, chat_id);
        PizzaOrder pizzaOrder = pizzaOrderService.getPizzaOrder(chat_id);

        String choice = pizzaOrder.getPizza().getPizzaName() + " з " + pizzaOrder.getPizzaIngredient();
        SendInvoice sendInvoice = sendPayments(choice, chat_id, pizzaOrder.getPizza().getPizzaPicture(), pizzaOrder.getPizza().getPizzaPrice());

        try {
            execute(sendInvoice);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}