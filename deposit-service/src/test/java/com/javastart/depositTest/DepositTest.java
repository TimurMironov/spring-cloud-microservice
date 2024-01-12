package com.javastart.depositTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.deposit.controller.DepositController;
import com.javastart.deposit.dto.AccountResponseDTO;
import com.javastart.deposit.dto.BillResponseDTO;
import com.javastart.deposit.dto.DepositRequestDTO;
import com.javastart.deposit.dto.DepositResponseDTO;
import com.javastart.deposit.exception.DepositServiceException;
import com.javastart.deposit.repository.DepositRepository;
import com.javastart.deposit.rest.AccountServiceClient;
import com.javastart.deposit.service.DepositService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DepositTest {

    @InjectMocks
    private DepositService depositService;

    @Mock
    private AccountServiceClient accountServiceClient;

    @Mock
    private DepositRepository depositRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    public void makeDepositOnlyWithBillIdTest() {
        Mockito.when(accountServiceClient.getBillById(ArgumentMatchers.anyLong())).thenReturn(createBillResponseDTO());

        DepositResponseDTO depositResponseDTO = depositService.makeDeposit(new DepositRequestDTO(null, 5L
                , BigDecimal.valueOf(100000)));
        Assertions.assertThat(depositResponseDTO.getEmail()).isEqualTo("test@yandex.ru");
        Assertions.assertThat(depositResponseDTO.getAmount()).isEqualTo(BigDecimal.valueOf(100000));
    }

    @Test
    public void makeDepositOnlyWithAccountId() {
        Mockito.when(accountServiceClient.getBillsByAccountId(ArgumentMatchers.anyLong()))
                .thenReturn(List.of(
                        new BillResponseDTO(1L, BigDecimal.valueOf(15000), true, OffsetDateTime.now(), true
                                , new AccountResponseDTO(1L, "test@yandex.ru", "89123456789"))
                        , new BillResponseDTO(2L, BigDecimal.valueOf(20000), false, OffsetDateTime.now(), true
                                , new AccountResponseDTO(1L, "false@yandex.ru", "89999999999"))
                ));

        DepositResponseDTO depositResponseDTO = depositService.makeDeposit(new DepositRequestDTO(5L, null
                , BigDecimal.valueOf(20000)));

        Assertions.assertThat(depositResponseDTO.getEmail()).isEqualTo("test@yandex.ru");
    }

    @Test
    public void shouldThrowDepositServiceExceptionTest() {
        org.junit.jupiter.api.Assertions.assertThrows(DepositServiceException.class
                , () -> depositService.makeDeposit(new DepositRequestDTO(null, null, BigDecimal.valueOf(100000))));
    }

    private BillResponseDTO createBillResponseDTO() {
        return BillResponseDTO.builder()
                .Id(1L)
                .amount(BigDecimal.valueOf(30000))
                .isDefault(true)
                .creationDate(OffsetDateTime.now())
                .overdraftEnabled(true)
                .account(new AccountResponseDTO(1L, "test@yandex.ru", "89123456789"))
                .build();
    }
}
