package com.example.online_bank.unit;

import com.example.online_bank.dto.limit.CreateLimitDto;
import com.example.online_bank.entity.Currency;
import com.example.online_bank.entity.Limit;
import com.example.online_bank.entity.Transaction;
import com.example.online_bank.repository.LimitRepository;
import com.example.online_bank.service.CurrencyService;
import com.example.online_bank.service.TransactionService;
import com.example.online_bank.service.impl.LimitServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class LimitServiceImplTest {

    @Mock
    private LimitRepository limitRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private LimitServiceImpl limitService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckLimitWhenNoLimitsExist() {
        Transaction transaction = Transaction.builder()
                .accountFrom(1L)
                .expenseCategory("product")
                .sum(BigDecimal.valueOf(100))
                .currencyShortname("USD")
                .build();

        Limit newLimit = Limit.builder()
                .limitDatetime(OffsetDateTime.now())
                .expenseCategory("product")
                .limitCurrencyShortname("USD")
                .account(1L)
                .limitSum(BigDecimal.valueOf(1000))
                .build();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        //Мокаем сервисы и репозиторий
        given(limitRepository.findAllByAccountAndExpenseCategory(anyLong(), anyString()))
                .willReturn(Collections.emptyList());
        given(limitRepository.findLimitWithMaxDatetimeByAccountAndCategory(transaction.getAccountFrom(),
                transaction.getExpenseCategory()))
                .willReturn(newLimit);
        given(limitRepository.save(any(Limit.class)))
                .willReturn(newLimit);
        given(transactionService.getAllByLimit(any()))
                .willReturn(transactions);
        given(currencyService.getExchangeRate(anyString(), anyString()))
                .willReturn(BigDecimal.valueOf(10));


         //проверяем сам метод
        Boolean result = limitService.checkLimit(transaction);



        assertFalse(result); // Since no limits exist, it should create one and return true
        verify(limitRepository, times(1)).save(any(Limit.class));
        verify(transactionService, times(1)).updateTransaction(transaction);
    }

    @Test
    public void testCheckLimitWhenLimitExistsAndExceeds() {

        //проверка на превышения лимита
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(1L);
        transaction.setExpenseCategory("product");
        transaction.setCurrencyShortname("USD");
        transaction.setSum(new BigDecimal("5000"));

        Limit limit = Limit.builder()
                .account(1L)
                .expenseCategory("product")
                .limitDatetime(OffsetDateTime.now())
                .limitSum(new BigDecimal("300"))
                .limitCurrencyShortname("USD")
                .build();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        given(limitRepository.findLimitWithMaxDatetimeByAccountAndCategory(anyLong(), anyString()))
                .willReturn(limit);
        given(transactionService.getAllByLimit(any(Limit.class)))
                .willReturn(transactions);
        given(currencyService.getExchangeRate(anyString(), anyString()))
                .willReturn(BigDecimal.valueOf(10));


        Boolean result = limitService.checkLimit(transaction);

        //Конечная сумма превышает лимит
        assertTrue(result);
        assertTrue(transaction.getLimitExceeded());
        verify(transactionService, times(1)).updateTransaction(transaction);
    }



    @Test
    public void testCreateLimit() {

        CreateLimitDto limitDto = new CreateLimitDto();
        limitDto.setAccount(1L);
        limitDto.setExpenseCategory("product");
        limitDto.setLimitSum(new BigDecimal("1000"));


        limitService.createLimit(limitDto);


        verify(limitRepository, times(1)).save(any(Limit.class));
    }

    @Test
    public void testCalculateTotalSumInGeneralCurrency() {

        List<Transaction> transactions = new ArrayList<>();
        //Если взять 2RUB == 1 usd
        transactions.add(Transaction.builder()
                .currencyShortname("USD")
                .sum(new BigDecimal("100"))
                .build());

        transactions.add(Transaction.builder()
                .currencyShortname("RUB")
                .sum(new BigDecimal("200"))
                .build());

        given(currencyService.getExchangeRate("USD", "USD"))
                .willReturn(new BigDecimal("1"));
        given(currencyService.getExchangeRate("RUB", "USD"))
                .willReturn(new BigDecimal("2"));

        // When
        BigDecimal totalSum = limitService.calculateTotalSumInGeneralCurrency(transactions, "USD");

        // Then
        assertEquals(new BigDecimal("200.00"), totalSum); // 100 USD + (200 EUR / 2)
    }
}
