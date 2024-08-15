package com.example.online_bank.repository;

import com.example.online_bank.entity.Limit;
import com.example.online_bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findAllByLimitAndLimitExceededIsFalse(Limit limit);
    @Query("SELECT t FROM Transaction t WHERE t.limitExceeded = true AND t.accountFrom = :account")
    List<Transaction> findFailedTransactionsByAccount(@Param("account") Long account);
}
