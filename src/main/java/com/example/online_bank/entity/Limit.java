package com.example.online_bank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "limits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Limit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "limit_sum")
    private BigDecimal limitSum;

    @Column(name = "limit_datetime")
    private OffsetDateTime limitDatetime;

    @Column(name = "currency_shortname")
    private String limitCurrencyShortname;

    @Column(name = "expense_category")
    private String expenseCategory;

    @Column(name = "account")
    private Long account;

    @OneToMany(mappedBy = "limit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
}
