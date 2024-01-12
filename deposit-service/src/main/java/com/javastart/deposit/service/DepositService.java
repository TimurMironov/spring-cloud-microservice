package com.javastart.deposit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.deposit.dto.*;
import com.javastart.deposit.entity.Deposit;
import com.javastart.deposit.exception.DepositServiceException;
import com.javastart.deposit.repository.DepositRepository;
import com.javastart.deposit.rest.AccountServiceClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
public class DepositService {

    private static final String TOPIC_EXCHANGE = "deposit.notify.exchanger";

    private static final String ROUTING_KEY = "deposit.notify.routingKey";


    private final DepositRepository depositRepository;

    private final AccountServiceClient accountServiceClient;

    private final RabbitTemplate rabbitTemplate;

    public DepositService(DepositRepository depositRepository, AccountServiceClient accountServiceClient
            , RabbitTemplate rabbitTemplate) {
        this.depositRepository = depositRepository;
        this.accountServiceClient = accountServiceClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public DepositResponseDTO makeDeposit(DepositRequestDTO depositRequestDTO) {
        if (depositRequestDTO.getAccountId() == null
                && depositRequestDTO.getBillId() == null) {
            throw new DepositServiceException("account_id or bill_id should not be null");
        }

        if (depositRequestDTO.getBillId() != null) {
            BillResponseDTO defaultBill = accountServiceClient.getBillById(depositRequestDTO.getBillId());
            defaultBill.setAmount(defaultBill.getAmount().add(depositRequestDTO.getAmount()));
            accountServiceClient.updateBill(depositRequestDTO.getBillId(), new BillRequestDTO(defaultBill));
            saveDepositAndSendRabbitMQMessage(depositRequestDTO, defaultBill);

            return createDepositResponseDTO(defaultBill.getAccount().getEmail(), depositRequestDTO.getAmount());
        }

        BillResponseDTO defaultBill = accountServiceClient.getBillsByAccountId(depositRequestDTO.getAccountId())
                .stream().filter(BillResponseDTO::getIsDefault).findAny()
                .orElseThrow(() -> new DepositServiceException("Can't find default bill"));
        defaultBill.setAmount(defaultBill.getAmount().add(depositRequestDTO.getAmount()));
        accountServiceClient.updateBill(defaultBill.getId(), new BillRequestDTO(defaultBill));
        saveDepositAndSendRabbitMQMessage(depositRequestDTO, defaultBill);

        return createDepositResponseDTO(defaultBill.getAccount().getEmail(), depositRequestDTO.getAmount());
    }

    private void saveDepositAndSendRabbitMQMessage(DepositRequestDTO depositRequestDTO, BillResponseDTO defaultBill) {
        Deposit deposit = new Deposit(depositRequestDTO.getAmount(), defaultBill.getId()
                , defaultBill.getAccount().getEmail());
        depositRepository.save(deposit);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, ROUTING_KEY, objectMapper
                    .writeValueAsString(createDepositResponseDTO(defaultBill.getAccount().getEmail()
                            , deposit.getAmount())));
        } catch (JsonProcessingException e) {
            throw new DepositServiceException("Can't send message to RabbitMQ");
        }
    }

    private DepositResponseDTO createDepositResponseDTO(String email, BigDecimal amount){
        return new DepositResponseDTO(email, amount);
    }
}
