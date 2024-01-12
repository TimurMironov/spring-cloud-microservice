package com.javastart.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.transaction.dto.BillRequestDTO;
import com.javastart.transaction.dto.BillResponseDTO;
import com.javastart.transaction.dto.TransactionRequestDTO;
import com.javastart.transaction.dto.TransactionResponseDTO;
import com.javastart.transaction.entity.Transaction;
import com.javastart.transaction.exception.TransactionServiceException;
import com.javastart.transaction.repository.TransactionRepository;
import com.javastart.transaction.rest.AccountServiceClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private static final String TOPIC_EXCHANGE = "transaction.notify.exchanger";

    private static final String ROUTING_KEY = "transaction.notify.routingKey";


    private final TransactionRepository transactionRepository;

    private final AccountServiceClient accountServiceClient;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository
            , AccountServiceClient accountServiceClient, RabbitTemplate rabbitTemplate) {
        this.transactionRepository = transactionRepository;
        this.accountServiceClient = accountServiceClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public TransactionResponseDTO makeTransaction(TransactionRequestDTO transactionRequestDTO) {
        if (transactionRequestDTO.getAccountId() == null
                && transactionRequestDTO.getBillId() == null) {
            throw new TransactionServiceException("account_id or bill_id should not be null");
        }

        if (transactionRequestDTO.getBillId() != null) {
            BillResponseDTO billResponseDTO = accountServiceClient.getBillById(transactionRequestDTO.getBillId());
            saveTransactionAndSendRabbitMqMessage(transactionRequestDTO, billResponseDTO);
            return createATransactionResponseDTO(billResponseDTO.getAccount().getEmail(), transactionRequestDTO.getAmount());
        }

        BillResponseDTO billResponseDTO = accountServiceClient.getBillsByAccountId(transactionRequestDTO
                        .getAccountId()).stream().filter(BillResponseDTO::getIsDefault).findAny()
                .orElseThrow(() -> new TransactionServiceException("Can't find default bill"));
        saveTransactionAndSendRabbitMqMessage(transactionRequestDTO, billResponseDTO);
        return createATransactionResponseDTO(billResponseDTO.getAccount().getEmail(), transactionRequestDTO.getAmount());
    }

    private void saveTransactionAndSendRabbitMqMessage(TransactionRequestDTO transactionRequestDTO, BillResponseDTO billResponseDTO) {
        if (billResponseDTO.getAmount().compareTo(transactionRequestDTO.getAmount()) >= 0) {
            billResponseDTO.setAmount(billResponseDTO.getAmount().subtract(transactionRequestDTO.getAmount()));
            accountServiceClient.updateBill(billResponseDTO.getId(), new BillRequestDTO(billResponseDTO));
            Transaction transaction = new Transaction(transactionRequestDTO.getAmount(), billResponseDTO.getId()
                    , billResponseDTO.getAccount().getEmail());
            transactionRepository.save(transaction);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, ROUTING_KEY, objectMapper
                        .writeValueAsString(createATransactionResponseDTO(billResponseDTO.getAccount().getEmail()
                                , transaction.getAmount())));
            } catch (JsonProcessingException e) {
                throw new TransactionServiceException("Can't send message to RabbitMQ");
            }
        } else {
            throw new TransactionServiceException("There is not enough money to make payment");
        }
    }

    private TransactionResponseDTO createATransactionResponseDTO(String email, BigDecimal amount) {
        return TransactionResponseDTO.builder()
                .email(email)
                .amount(amount)
                .build();
    }
}
