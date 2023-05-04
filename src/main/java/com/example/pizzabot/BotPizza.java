package com.example.pizzabot;

import com.example.pizzabot.buttons.PizzasButtons;
import com.example.pizzabot.mail_sender.EmailSender;
import com.example.pizzabot.model.PizzaOrder;
import com.example.pizzabot.payments.SendPayments;
import com.example.pizzabot.service.PizzaOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class BotPizza extends TelegramLongPollingBot implements PizzasButtons, SendPayments {

    private List<PizzaOrder> pizzaOrders = new ArrayList<PizzaOrder>(List.of(new PizzaOrder()));
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

        tryPreCheckout(update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message;
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if (message_text.equals("/start") && !update.hasPreCheckoutQuery()) {
                message = initialMessage(chat_id, "Вітаю, оберіть найближчу піццерію"); // вибір найближчої піцерії
                sendMessage(message);
            } else if (message_text.length() > 0 && BotPizza.keyboardPizzaAddresses.contains(new KeyboardButton(message_text))) {
                message = createOrderByAddress(message_text, chat_id); // вибір піцци
                sendMessage(message);
            } else if (message_text.length() > 0 && BotPizza.keyboardPizzaButtons.contains(new KeyboardButton(message_text))) {
                message = updateOrderByPizza(message_text, chat_id); // запис вибору піцци
                sendMessage(message);
            } else if (message_text.length() > 0 && BotPizza.keyboardReplyIngredients.contains(new KeyboardButton(message_text))) {
                updateOrderByIngredient(message_text, chat_id);  // запис вибору інгредієнту
            } else if (message_text.length() == 10) {
                updateOrderByPhoneNumber(message_text, chat_id); // фінальне повідомлення про вибір користувача
            }

        }
    }

    private void tryPreCheckout(Update update) {
        if (update.hasPreCheckoutQuery()) {
            AnswerPreCheckoutQuery answerPreCheckoutQuery = new
                    AnswerPreCheckoutQuery(update.getPreCheckoutQuery().getId(), true);
            sendMessage(answerPreCheckoutQuery);
        } else if (update.getMessage().hasSuccessfulPayment()) {
            Integer orderAmount = update.getMessage().getSuccessfulPayment().getTotalAmount();
            long chat_id = update.getMessage().getChatId();
            pizzaOrderService.updatePizzaOrderWhenPayed(chat_id);
            PizzaOrder pizzaOrder = pizzaOrderService.getPizzaOrder(chat_id);
            mailSender.sendMessage("Нове замовлення " + pizzaOrder.getPizza().getPizzaName() + " та інгредієнт " + pizzaOrder.getPizzaIngredient() +
                    " на відділення " + pizzaOrder.getPizzasAddress() + " на оплачену суму " + orderAmount);
            sendMessage(initialMessage(chat_id, "Для наступного замовлення введіть /start"));
        }
    }

    private void sendMessage(BotApiMethod<?> botApiMethod) { // надсилання повідомлень
        try {
            execute(botApiMethod);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage initialMessage(Long chat_id, String text) { // надсилання початкового повідомлення
        SendMessage message = new SendMessage(String.valueOf(chat_id), text);
        message.setReplyMarkup(BotPizza.replyAddresses);
        return message;
    }

    private SendMessage createOrderByAddress(String pizzas_address, Long chat_id) { // створення ордеру за Адресою
        pizzaOrderService.newPizzaOrder(pizzas_address, chat_id);
        SendMessage message = new SendMessage(String.valueOf(chat_id), "Оберіть піццу");
        message.setReplyMarkup(BotPizza.replyPizzaChoice);
        return message;
    }

    private SendMessage updateOrderByPizza(String message_text, Long chat_id) { // оновлення ордеру за Піццою
        pizzaOrderService.updatePizzaOrderByPizza(message_text, chat_id);
        String replyText = "Ви обрали " + message_text.toLowerCase() + " піццу, оберіть додатковий інгредієнт";
        SendMessage message = new SendMessage(String.valueOf(chat_id), replyText);
        message.setReplyMarkup(BotPizza.ingredientsKeyboardMarkup);
        return message;
    }

    private void updateOrderByIngredient(String message_text, Long chat_id) { // оновлення ордеру за Інгредієнтом
        pizzaOrderService.updatePizzaOrderIngredient(message_text.equals("Без інгредієнту") ? " без доп. інгредієнтів" : message_text, chat_id);
        sendMessage(initialMessage(chat_id, "Введіть номер телефону без коду країни"));

    }

    private void updateOrderByPhoneNumber(String phoneNumber, Long chat_id) { // оновлення ордеру за номером
        pizzaOrderService.updatePizzaOrderByPhone(phoneNumber, chat_id);
        PizzaOrder pizzaOrder = pizzaOrderService.getPizzaOrder(chat_id);
        String choice = pizzaOrder.getPizza().getPizzaName() + " з " + pizzaOrder.getPizzaIngredient();
        SendInvoice sendInvoice = sendPayments(choice, chat_id, pizzaOrder.getPizza().getPizzaPicture(), pizzaOrder.getPizza().getPizzaPrice());

        sendMessage(sendInvoice);
    }

}