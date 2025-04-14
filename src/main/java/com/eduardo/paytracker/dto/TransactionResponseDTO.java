package com.eduardo.paytracker.dto;

import com.eduardo.paytracker.model.Transaction;
import com.eduardo.paytracker.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO(
        Long id,
        String title,
        String description,
        BigDecimal amount,
        LocalDate createdAt,
        LocalDate dueDate,
        TransactionType type,
        Long userId
) {

    public TransactionResponseDTO(Transaction transaction){
        this(transaction.getId(), transaction.getTitle(), transaction.getDescription(), transaction.getAmount(),
                transaction.getCreatedAt(), transaction.getDueDate(), transaction.getType(), transaction.getUser().getId());
    }

}
