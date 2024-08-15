package com.example.online_bank.dto.transaction;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FailedTransactionsDto {
    private Long accountTo;
    private BigDecimal transferAmount;
    private String currency;
    private String expenseCategory;
    private BigDecimal limitAmount;
    private String limitDate;
    private String limitCurrency;

}
