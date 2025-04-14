package com.eduardo.paytracker.dto;

import com.eduardo.paytracker.model.enums.TransactionType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDTO(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull @Positive BigDecimal amount,
        @NotNull @Future LocalDate dueDate,
        @NotNull TransactionType type
) {
}
