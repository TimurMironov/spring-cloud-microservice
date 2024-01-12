package com.javastart.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.notification.config.TransactionRabbitMqConfig;
import com.javastart.notification.dto.TransactionResponseDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class TransactionMessageHandler {

    private final JavaMailSender javaMailSender;

    public TransactionMessageHandler(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = TransactionRabbitMqConfig.QUEUE)
    public void transactionMessageReceiver(Message message) throws JsonProcessingException {
        byte [] bytesMessage = message.getBody();
        String jsonBody = new String(bytesMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        TransactionResponseDTO transaction = objectMapper.readValue(jsonBody, TransactionResponseDTO.class);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(transaction.getEmail());
        mailMessage.setFrom("testMail@gmail.com");
        mailMessage.setSubject("Transaction");
        mailMessage.setText("Transaction is confirmed - sum of transaction: " + transaction.getAmount());

        javaMailSender.send(mailMessage);
    }
}
