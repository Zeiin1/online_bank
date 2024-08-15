package com.example.online_bank.controller;

import com.example.online_bank.dto.limit.CreateLimitDto;
import com.example.online_bank.entity.ExpenseCategory;
import com.example.online_bank.service.LimitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/limits")
public class LimitController {
    private final LimitService limitService;
    @PostMapping
    public ResponseEntity<String> createLimit(@RequestBody CreateLimitDto limitDto)
    {
        if(!limitDto.getExpenseCategory().equals(String.valueOf(ExpenseCategory.service)) &&
                !limitDto.getExpenseCategory().equals(String.valueOf(ExpenseCategory.product)))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Limit category should be service or product");
        limitService.createLimit(limitDto);
        return ResponseEntity.ok("Limit created successfully!");
    }

}
