package com.eduardo.paytracker.service;

import com.eduardo.paytracker.dto.TransactionRequestDTO;
import com.eduardo.paytracker.dto.TransactionResponseDTO;
import com.eduardo.paytracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionResponseDTO createTransaction(TransactionRequestDTO data) {

        return new TransactionResponseDTO(null, null, null, null,null,null,null, null);
    }
}
