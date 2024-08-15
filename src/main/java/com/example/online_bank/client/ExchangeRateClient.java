package com.example.online_bank.client;

import com.example.online_bank.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "exchangeRateClient", url = "https://api.exchangeratesapi.io")
public interface ExchangeRateClient {
    @GetMapping("/latest")
    Map<String, Object> getLatestRates(@RequestParam("base") String baseCurrency);
}

