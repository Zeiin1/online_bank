package com.example.online_bank.service;

import com.example.online_bank.dto.transaction.FailedTransactionsDto;
import com.example.online_bank.dto.transaction.TransactionDto;
import com.example.online_bank.entity.Limit;
import com.example.online_bank.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(TransactionDto transactionDto);

    List<Transaction> getAllByLimit(Limit limit);

    void updateTransaction(Transaction completed);

    List<FailedTransactionsDto> getFailedTransactions(Long account);
}
