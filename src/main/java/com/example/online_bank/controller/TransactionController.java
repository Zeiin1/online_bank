package com.example.online_bank.controller;

import com.example.online_bank.dto.transaction.FailedTransactionsDto;
import com.example.online_bank.dto.transaction.TransactionDto;
import com.example.online_bank.dto.transaction.TransactionDtoCompleted;
import com.example.online_bank.entity.Limit;
import com.example.online_bank.entity.Transaction;
import com.example.online_bank.service.LimitService;
import com.example.online_bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final LimitService limitService;

    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestBody TransactionDto transactionDto) {
        if (transactionDto == null) {
            return ResponseEntity.badRequest().build();
        }

        Transaction completed = transactionService.createTransaction(transactionDto);
        Boolean isLimitExceeded = limitService.checkLimit(completed);

        if (isLimitExceeded) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                    .body("Transaction created, but limit exceeded.");
        } else {
            return ResponseEntity.ok("Transaction created successfully.");
        }
    }
    @GetMapping("/failed")
    public ResponseEntity<List<FailedTransactionsDto>> getFailedTransactions(@RequestParam Long account)
    {
        List<FailedTransactionsDto> failedTransactionsDtos =
                transactionService.getFailedTransactions(account);
        return ResponseEntity.ok(failedTransactionsDtos);
    }



}
