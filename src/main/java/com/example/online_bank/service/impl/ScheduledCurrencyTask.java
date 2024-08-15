package com.example.online_bank.service.impl;

import com.example.online_bank.service.impl.CurrencyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledCurrencyTask {

    private final CurrencyServiceImpl currencyService;

    @Scheduled(cron = "0 0 0 * * *") // Runs daily at midnight
    public void updateCurrenciesDaily() {
        currencyService.fetchAndStoreCurrencies();
    }
}