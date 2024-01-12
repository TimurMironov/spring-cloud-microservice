package com.javastart.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.notification.config.DepositRabbitMqConfig;
import com.javastart.notification.dto.DepositResponseDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class DepositMessageHandler {


    private final JavaMailSender javaMailSender;

    @Autowired
    public DepositMessageHandler(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = DepositRabbitMqConfig.QUEUE)
    public void depositMessageReceiver(Message message) throws JsonProcessingException {
        byte[] messageBytesBody = message.getBody();
        String jsonBody = new String(messageBytesBody);
        ObjectMapper objectMapper = new ObjectMapper();
        DepositResponseDTO deposit = objectMapper.readValue(jsonBody, DepositResponseDTO.class);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(deposit.getEmail());
        mailMessage.setFrom("testMail@gmail.com");
        mailMessage.setSubject("Deposit");
        mailMessage.setText("Deposit is confirmed - sum of deposit: " + deposit.getAmount());

        javaMailSender.send(mailMessage);
    }
}
