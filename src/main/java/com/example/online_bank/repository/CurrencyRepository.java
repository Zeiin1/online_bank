package com.example.online_bank.repository;

import com.example.online_bank.entity.Currencies;
import com.example.online_bank.entity.Currency;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currencies,Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Currencies c SET c.rate = :currencyRate WHERE c.currencyCode = :currencyCode")
    int updateCurrencyRate(String currencyCode, BigDecimal currencyRate);

    Optional<Currencies> findByCurrencyCode(String currencyCode);
}
