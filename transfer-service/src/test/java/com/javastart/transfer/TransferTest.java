package com.javastart.transfer;

import com.javastart.transfer.dto.AccountResponseDTO;
import com.javastart.transfer.dto.BillResponseDTO;
import com.javastart.transfer.dto.TransferRequestDTO;
import com.javastart.transfer.dto.TransferResponseDTO;
import com.javastart.transfer.exception.TransferServiceException;
import com.javastart.transfer.repository.TransferRepository;
import com.javastart.transfer.rest.AccountServiceClient;
import com.javastart.transfer.service.TransferService;
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

@ExtendWith(MockitoExtension.class)
public class TransferTest {

    @InjectMocks
    private TransferService transferService;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AccountServiceClient accountServiceClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    public void makeTransferTest() {
        Mockito.when(accountServiceClient.getBillById(1L)).thenReturn(createBillResponseDtoFrom());
        Mockito.when(accountServiceClient.getBillById(2L)).thenReturn(createBillResponseDtoTo());

        TransferResponseDTO transferDTO = transferService.makeTransfer(new TransferRequestDTO(1L, 2L
                , BigDecimal.valueOf(20000)));
        Assertions.assertThat(transferDTO.getEmailFrom()).isEqualTo("test@yandex.ru");
        Assertions.assertThat(transferDTO.getEmailTo()).isEqualTo("test2@yandex.ru");
        Assertions.assertThat(transferDTO.getAmount()).isEqualTo(BigDecimal.valueOf(20000));
    }

    @Test
    public void shouldThrowTransactionServiceExceptionNotEnoughMoneyToMakeTransferTest() {
        Mockito.when(accountServiceClient.getBillById(1L)).thenReturn(createBillResponseDtoFrom());
        Mockito.when(accountServiceClient.getBillById(2L)).thenReturn(createBillResponseDtoTo());

        org.junit.jupiter.api.Assertions.assertThrows(TransferServiceException.class
                , () -> transferService.makeTransfer(new TransferRequestDTO(1L, 2L, BigDecimal.valueOf(40000))));
    }

    @Test
    public void shouldThrowTransferServiceException() {
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class
                , () -> transferService.makeTransfer(new TransferRequestDTO(1L, null, BigDecimal.valueOf(12000))));
    }

    private BillResponseDTO createBillResponseDtoFrom() {
        return BillResponseDTO.builder()
                .Id(1L)
                .amount(BigDecimal.valueOf(30000))
                .isDefault(true)
                .creationDate(OffsetDateTime.now())
                .overdraftEnabled(true)
                .account(new AccountResponseDTO(1L, "test@yandex.ru", "111111111"))
                .build();
    }


    private BillResponseDTO createBillResponseDtoTo() {
        return BillResponseDTO.builder()
                .Id(2L)
                .amount(BigDecimal.valueOf(40000))
                .isDefault(true)
                .creationDate(OffsetDateTime.now())
                .overdraftEnabled(true)
                .account(new AccountResponseDTO(2L, "test2@yandex.ru", "222222222"))
                .build();
    }
}
