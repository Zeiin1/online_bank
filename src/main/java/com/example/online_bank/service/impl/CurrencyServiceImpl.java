package com.example.online_bank.service.impl;

import com.example.online_bank.client.ExchangeRateClient;
import com.example.online_bank.entity.Currencies;
import com.example.online_bank.entity.Currency;
import com.example.online_bank.repository.CurrencyRepository;
import com.example.online_bank.service.CurrencyService;
import feign.Request;
import feign.Response;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;



@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService{
    private final CurrencyRepository currencyRepository;


    @PostConstruct
    //чтобы добавить записи когда приложение запуститься
    public void init() throws IOException {
        fetchAndStoreCurrencies();
    }

    private static final String API_KEY = "gPHWRyO493Ls0qTJy0QxmJthloL0RroG";
    private static final String API_URL = "https://api.apilayer.com/exchangerates_data/latest?symbols=KZT,RUB,USD&base=USD";

    public void fetchAndStoreCurrencies() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, JsonNode.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            JsonNode rates = response.getBody().get("rates");

            Currencies kztCurrency = Currencies.builder()
                    .currencyCode(String.valueOf(Currency.KZT))
                    .rate(rates.get("KZT").decimalValue())
                    .timestamp(LocalDateTime.now())
                    .build();

            Currencies rubCurrency = Currencies.builder()
                    .currencyCode(String.valueOf(Currency.RUB))
                    .rate(rates.get("RUB").decimalValue())
                    .timestamp(LocalDateTime.now())
                    .build();

            Currencies usdCurrency = Currencies.builder()
                    .currencyCode(String.valueOf(Currency.USD))
                    .rate(BigDecimal.valueOf(1.0))
                    .timestamp(LocalDateTime.now())
                    .build();
            //добавить проверку на наличие записи, если есть то обновить только rate
            List<Currencies> currencies = currencyRepository.findAll();
            if(currencies.isEmpty()){
            currencyRepository.save(kztCurrency);
            currencyRepository.save(rubCurrency);
            currencyRepository.save(usdCurrency);
            }
            else {
                currencyRepository.updateCurrencyRate(kztCurrency.getCurrencyCode(),kztCurrency.getRate());
                currencyRepository.updateCurrencyRate(rubCurrency.getCurrencyCode(),rubCurrency.getRate());
            }

            System.out.println("Currencies saved successfully!");
        } else {
            System.err.println("Request failed with code: " + response.getStatusCodeValue());
        }
    }

    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {

        if (fromCurrency.equals(toCurrency)) {
            return BigDecimal.ONE;
        }
        Optional<Currencies> rate = currencyRepository.findByCurrencyCode(fromCurrency);
        return rate.map(Currencies::getRate).orElse(BigDecimal.ZERO);
    }
}


