package com.example.online_bank.dto.transaction;

import com.example.online_bank.entity.Currency;
import com.example.online_bank.entity.ExpenseCategory;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionDto {
    private Long accountFrom;
    private Long accountTo;
    private Currency currencyShortname;
    private BigDecimal sum;
    private ExpenseCategory expenseCategory;
}
