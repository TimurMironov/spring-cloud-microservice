package com.javastart.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.notification.config.TransferRabbitMqConfig;
import com.javastart.notification.dto.TransferResponseDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class TransferMessageHandler {

    private final JavaMailSender javaMailSender;

    @Autowired
    public TransferMessageHandler(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = TransferRabbitMqConfig.QUEUE)
    public void transferMessageReceiver(Message message) throws JsonProcessingException {
        byte[] messageBytesBody = message.getBody();
        String jsonBody = new String(messageBytesBody);

        ObjectMapper objectMapper = new ObjectMapper();

        TransferResponseDTO transferInfo = objectMapper.readValue(jsonBody, TransferResponseDTO.class);

        SimpleMailMessage mailMessageFrom = new SimpleMailMessage();
        mailMessageFrom.setTo(transferInfo.getEmailFrom());
        mailMessageFrom.setFrom("testMail@gmail.com");
        mailMessageFrom.setSubject("Transaction");
        mailMessageFrom.setText("Transfer is confirmed - sum of transfer: " + transferInfo.getAmount());

        SimpleMailMessage mailMessageTo = new SimpleMailMessage();
        mailMessageTo.setTo(transferInfo.getEmailTo());
        mailMessageTo.setFrom("testMail@gmail.com");
        mailMessageTo.setSubject("Transaction");
        mailMessageTo.setText("Transfer is confirmed - sum of transfer: " + transferInfo.getAmount());

        javaMailSender.send(mailMessageFrom);
        javaMailSender.send(mailMessageTo);
    }
}
