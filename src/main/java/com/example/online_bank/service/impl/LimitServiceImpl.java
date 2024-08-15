package com.example.online_bank.service.impl;

import com.example.online_bank.dto.limit.CreateLimitDto;
import com.example.online_bank.entity.*;
import com.example.online_bank.repository.LimitRepository;
import com.example.online_bank.repository.TransactionRepository;
import com.example.online_bank.service.CurrencyService;
import com.example.online_bank.service.LimitService;
import com.example.online_bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class LimitServiceImpl implements LimitService {

    private final LimitRepository limitRepository;
    private final TransactionService transactionService;
    private final CurrencyService currencyService;

    @Override
    public Boolean checkLimit(Transaction completed) {
        //if bank account newly created, and its his first transaction
        List<Limit> limits = limitRepository.findAllByAccountAndExpenseCategory(completed.getAccountFrom(),
                completed.getExpenseCategory());
        if(limits.isEmpty())
        {

            Limit limit = Limit.builder()
                    .account(completed.getAccountFrom())
                    .limitSum(BigDecimal.valueOf(1000))
                    .expenseCategory(completed.getExpenseCategory())
                    .limitCurrencyShortname(String.valueOf(Currency.USD))
                    .limitDatetime(OffsetDateTime.now())
                    .build();
            limitRepository.save(limit);

        }
        //setting limit into transaction
        Limit limit = limitRepository.findLimitWithMaxDatetimeByAccountAndCategory(completed.getAccountFrom(),
                completed.getExpenseCategory());
        completed.setLimit(limit);
        List<Transaction> transactions = transactionService.getAllByLimit(limit);
        transactions.add(completed);
        //calculate transactions sum
        //the total sum will be calculated based currency setted in limit
        //String currency = limit.getLimitCurrencyShortname();

        BigDecimal totalTransactionsSum = calculateTotalSumInGeneralCurrency(transactions,
                limit.getLimitCurrencyShortname());
        if(totalTransactionsSum.compareTo(limit.getLimitSum()) == 1){
            completed.setLimitExceeded(true);
            transactionService.updateTransaction(completed);
            return true;
        }
        transactionService.updateTransaction(completed);
        return false;
    }

    @Override
    public void createLimit(CreateLimitDto limitDto) {
        Limit limit = Limit.builder()
                .limitSum(limitDto.getLimitSum())
                .limitCurrencyShortname(String.valueOf(Currency.USD))
                .account(limitDto.getAccount())
                .expenseCategory(limitDto.getExpenseCategory())
                .limitDatetime(OffsetDateTime.now())
                .build();
        limitRepository.save(limit);
    }

    public BigDecimal calculateTotalSumInGeneralCurrency(List<Transaction> transactions, String generalCurrency) {
        BigDecimal totalSum = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            String transactionCurrency = transaction.getCurrencyShortname();
            BigDecimal transactionAmount = transaction.getSum();

            // Get exchange rate from transaction currency to general currency
            BigDecimal exchangeRate = currencyService.getExchangeRate(transactionCurrency, generalCurrency);


            // Convert transaction amount to general currency with proper scaling and rounding
            BigDecimal convertedAmount = transactionAmount.divide(exchangeRate, 4, RoundingMode.HALF_UP);

            // Add to total sum
            totalSum = totalSum.add(convertedAmount);
        }
        return totalSum.setScale(2, RoundingMode.HALF_UP);
    }

}
