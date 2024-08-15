package com.example.online_bank.service;

import java.math.BigDecimal;

public interface CurrencyService {
    BigDecimal getExchangeRate(String transactionCurrency, String generalCurrency);
}
