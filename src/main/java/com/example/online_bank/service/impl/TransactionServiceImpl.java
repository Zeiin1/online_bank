package com.example.online_bank.service.impl;

import com.example.online_bank.dto.transaction.FailedTransactionsDto;
import com.example.online_bank.dto.transaction.TransactionDto;
import com.example.online_bank.entity.Limit;
import com.example.online_bank.entity.Transaction;
import com.example.online_bank.repository.TransactionRepository;
import com.example.online_bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private final TransactionRepository transactionRepository;
    @Override
    public Transaction createTransaction(TransactionDto transactionDto) {
        OffsetDateTime datetime = OffsetDateTime.now();
        Transaction transaction = Transaction.builder()
                .accountFrom(transactionDto.getAccountFrom())
                .accountTo(transactionDto.getAccountTo())
                .currencyShortname(String.valueOf(transactionDto.getCurrencyShortname()))
                .expenseCategory(String.valueOf(transactionDto.getExpenseCategory()))
                .sum(transactionDto.getSum())
                .datetime(datetime)
                .limitExceeded(false)
                .build();
        transactionRepository.save(transaction);
        return transaction;
    }

    @Override
    public List<Transaction> getAllByLimit(Limit limit) {
        //check for limit_exceeded = false
        return transactionRepository.findAllByLimitAndLimitExceededIsFalse(limit);
    }

    @Override
    public void updateTransaction(Transaction completed) {
        transactionRepository.save(completed);
    }

    @Override
    public List<FailedTransactionsDto> getFailedTransactions(Long account) {

        List<Transaction> failedTransactions = transactionRepository.findFailedTransactionsByAccount(account);

        return failedTransactions.stream()
                .map(transaction -> FailedTransactionsDto.builder()
                        .accountTo(transaction.getAccountTo())
                        .transferAmount(transaction.getSum())
                        .currency(transaction.getCurrencyShortname())
                        .expenseCategory(transaction.getExpenseCategory())
                        .limitAmount(transaction.getLimit().getLimitSum())
                        .limitDate(transaction.getLimit().getLimitDatetime().toString())
                        .limitCurrency(transaction.getLimit().getLimitCurrencyShortname())
                        .build())
                .collect(Collectors.toList());
    }
}
