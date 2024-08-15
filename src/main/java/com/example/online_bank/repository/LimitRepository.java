package com.example.online_bank.repository;

import com.example.online_bank.entity.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimitRepository extends JpaRepository<Limit,Long> {
    List<Limit> findAllByAccountAndExpenseCategory(Long account,String expenseCategory);
    @Query("SELECT l FROM Limit l WHERE l.account = :account AND l.expenseCategory = :expenseCategory " +
            " AND l.limitDatetime = " +
            "(SELECT MAX(l2.limitDatetime) FROM Limit l2 WHERE l2.account = :account " +
            " AND l2.expenseCategory = :expenseCategory)")
    Limit findLimitWithMaxDatetimeByAccountAndCategory(@Param("account") Long account,
                                                       @Param("expenseCategory") String expenseCategory);

}
