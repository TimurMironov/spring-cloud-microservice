package com.javastart.transfer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.transfer.dto.BillRequestDTO;
import com.javastart.transfer.dto.BillResponseDTO;
import com.javastart.transfer.dto.TransferRequestDTO;
import com.javastart.transfer.dto.TransferResponseDTO;
import com.javastart.transfer.entity.Transfer;
import com.javastart.transfer.exception.TransferServiceException;
import com.javastart.transfer.repository.TransferRepository;
import com.javastart.transfer.rest.AccountServiceClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private static final String TOPIC_EXCHANGE = "transfer.notify.exchanger";

    private static final String ROUTING_KEY = "transfer.notify.routingKey";


    private final TransferRepository transferRepository;

    private final AccountServiceClient accountServiceClient;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TransferService(TransferRepository transferRepository, AccountServiceClient accountServiceClient
            , RabbitTemplate rabbitTemplate) {
        this.transferRepository = transferRepository;
        this.accountServiceClient = accountServiceClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public TransferResponseDTO makeTransfer(TransferRequestDTO transferRequestDTO) {
        BillResponseDTO billFrom = accountServiceClient.getBillById(transferRequestDTO.getBillFrom());
        BillResponseDTO billTo = accountServiceClient.getBillById(transferRequestDTO.getBillTo());

        if (billFrom.getAmount().compareTo(transferRequestDTO.getAmount()) >= 0) {
            billFrom.setAmount(billFrom.getAmount().subtract(transferRequestDTO.getAmount()));
            billTo.setAmount(billTo.getAmount().add(transferRequestDTO.getAmount()));

            accountServiceClient.updateBill(billFrom.getId(), new BillRequestDTO(billFrom));
            accountServiceClient.updateBill(billTo.getId(), new BillRequestDTO(billTo));

            saveTransferAndSendRabbitMqMessage(transferRequestDTO, billFrom, billTo);
            return new TransferResponseDTO(transferRequestDTO.getAmount(), billFrom.getAccount().getEmail()
                    , billTo.getAccount().getEmail());
        } else {
            throw new TransferServiceException("There is not enough money to make transfer");
        }
    }

    private void saveTransferAndSendRabbitMqMessage(TransferRequestDTO transferRequestDTO
            , BillResponseDTO billFrom, BillResponseDTO billTo) {
        Transfer transfer = new Transfer(transferRequestDTO.getAmount(), billFrom.getId()
                , billTo.getId(), billFrom.getAccount().getEmail(), billTo.getAccount().getEmail());

        transferRepository.save(transfer);

        ObjectMapper objectMapper = new ObjectMapper();
        TransferResponseDTO transferInfo = new TransferResponseDTO(transfer.getAmount()
                , transfer.getEmailFrom(), transfer.getEmailTo());
        try {
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, ROUTING_KEY, objectMapper.writeValueAsString(transferInfo));
        } catch (JsonProcessingException e) {
            throw new TransferServiceException("Can't send message to RabbitMQ");
        }
    }
}
