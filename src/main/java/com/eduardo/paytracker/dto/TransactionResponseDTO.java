package com.eduardo.paytracker.dto;

import com.eduardo.paytracker.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO(
        Long id,
        String description,
        BigDecimal amount,
        LocalDate createdAt,
        LocalDate dueDate,
        TransactionType type,
        Long userId
) {
}
