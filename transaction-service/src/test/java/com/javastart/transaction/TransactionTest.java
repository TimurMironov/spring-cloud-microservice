package com.javastart.transaction;

import com.javastart.transaction.dto.AccountResponseDTO;
import com.javastart.transaction.dto.BillResponseDTO;
import com.javastart.transaction.dto.TransactionRequestDTO;
import com.javastart.transaction.dto.TransactionResponseDTO;
import com.javastart.transaction.exception.TransactionServiceException;
import com.javastart.transaction.repository.TransactionRepository;
import com.javastart.transaction.rest.AccountServiceClient;
import com.javastart.transaction.service.TransactionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TransactionTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private AccountServiceClient accountServiceClient;

    @Test
    public void makeDepositOnlyWithBillIdTest() {
        Mockito.when(accountServiceClient.getBillById(ArgumentMatchers.anyLong())).thenReturn(createBillResponseDTO());

        TransactionResponseDTO transactionDTO = transactionService
                .makeTransaction(new TransactionRequestDTO(null, 2L, BigDecimal.valueOf(20000)));
        Assertions.assertThat(transactionDTO.getEmail()).isEqualTo("test@yandex.ru");
    }

    @Test
    public void makeDepositOnlyWithAccountId() {
        Mockito.when(accountServiceClient.getBillsByAccountId(ArgumentMatchers.anyLong()))
                .thenReturn(createListOfBillResponseDTO());

        TransactionResponseDTO transactionResponseDTO = transactionService
                .makeTransaction(new TransactionRequestDTO(5L, null, BigDecimal.valueOf(20000)));

        Assertions.assertThat(transactionResponseDTO.getEmail()).isEqualTo("test@yandex.ru");
    }

    @Test
    public void shouldThrowTransactionServiceExceptionBillIdOrAccountIdShouldNotBeNullTest() {
        org.junit.jupiter.api.Assertions.assertThrows(TransactionServiceException.class
                , () -> transactionService.makeTransaction(new TransactionRequestDTO(null, null
                        , BigDecimal.valueOf(20000))));
    }

    @Test
    public void shouldThrowTransactionServiceExceptionNotEnoughMoneyToMakeTransactionTest() {
        Mockito.when(accountServiceClient.getBillById(ArgumentMatchers.anyLong())).thenReturn(createBillResponseDTO());

        org.junit.jupiter.api.Assertions.assertThrows(TransactionServiceException.class
                , () -> transactionService.makeTransaction(new TransactionRequestDTO(null, 5L
                        , BigDecimal.valueOf(50000))));
    }


    private BillResponseDTO createBillResponseDTO() {
        return BillResponseDTO.builder()
                .Id(1L)
                .amount(BigDecimal.valueOf(30000))
                .isDefault(true)
                .creationDate(OffsetDateTime.now())
                .overdraftEnabled(true)
                .account(new AccountResponseDTO(1L, "test@yandex.ru", "111111111"))
                .build();
    }

    private List<BillResponseDTO> createListOfBillResponseDTO() {
        return List.of(new BillResponseDTO(1L, BigDecimal.valueOf(15000), false, OffsetDateTime.now(), true
                        , new AccountResponseDTO(1L, "false@yandex.ru", "89123456789"))
                , new BillResponseDTO(2L, BigDecimal.valueOf(20000), true, OffsetDateTime.now(), true
                        , new AccountResponseDTO(1L, "test@yandex.ru", "89999999999"))
        );
    }
}
