package com.example.online_bank.dto.limit;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateLimitDto {
    private Long account;
    private BigDecimal limitSum;
    private String expenseCategory;
}
