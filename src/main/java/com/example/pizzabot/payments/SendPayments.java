package com.example.pizzabot.payments;

import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.util.List;

public interface SendPayments {

    default SendInvoice sendPayments(String choice, Long chat_id, String pictureUrl, Integer amount) {
        List<LabeledPrice> prices = List.of(new LabeledPrice(choice, amount * 100));

        SendInvoice sendInvoice = new SendInvoice(String.valueOf(chat_id), "Замовлення", choice, "оплата",
                "632593626:TEST:sandbox_i92106113245", "parameter", "UAH", prices);
        sendInvoice.setPhotoUrl(pictureUrl);
        sendInvoice.setPhotoHeight(300);
        sendInvoice.setPhotoSize(300);
        sendInvoice.setPhotoWidth(300);

        return sendInvoice;
    }

}