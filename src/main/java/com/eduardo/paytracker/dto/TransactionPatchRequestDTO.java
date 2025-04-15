package com.eduardo.paytracker.dto;

import com.eduardo.paytracker.model.enums.TransactionType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionPatchRequestDTO(
        String title,
        String description,
        @Positive BigDecimal amount,
        @Future LocalDate dueDate,
        TransactionType type
) {
}
