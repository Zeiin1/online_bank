package com.example.online_bank.service;

import com.example.online_bank.dto.limit.CreateLimitDto;
import com.example.online_bank.entity.Transaction;

public interface LimitService {
    Boolean checkLimit(Transaction completed);

    void createLimit(CreateLimitDto limitDto);
}
