package com.eduardo.paytracker.controller;

import com.eduardo.paytracker.dto.TransactionRequestDTO;
import com.eduardo.paytracker.dto.TransactionResponseDTO;
import com.eduardo.paytracker.repository.TransactionRepository;
import com.eduardo.paytracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    private ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid TransactionRequestDTO data){
        var createdTransaction = transactionService.createTransaction(data);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

}
