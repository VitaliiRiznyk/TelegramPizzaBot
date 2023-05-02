package com.example.pizzabot.mail_sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendMessage(String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("epizzabot@gmail.com");
        message.setTo("vitaliy.riznyk@gmail.com");
        message.setSubject("Нове замовлення");
        message.setText(text);
        javaMailSender.send(message);
    }

}