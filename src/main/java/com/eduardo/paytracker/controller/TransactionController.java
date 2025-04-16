package com.eduardo.paytracker.controller;

import com.eduardo.paytracker.dto.TransactionRequestDTO;
import com.eduardo.paytracker.dto.TransactionResponseDTO;
import com.eduardo.paytracker.dto.TransactionPatchRequestDTO;
import com.eduardo.paytracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid TransactionRequestDTO data){
        var createdTransaction = transactionService.createTransaction(data);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> getAll(Pageable pageable){
        var transactions = transactionService.getAllTransactions(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(@PathVariable Long id){
        var transaction = transactionService.getTransactionById(id);

        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> specificUpdate(@RequestBody @Valid TransactionPatchRequestDTO data, @PathVariable Long id){
        var transaction = transactionService.transactionSpecificUpdate(data, id);

        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> completeUpdate(@RequestBody @Valid TransactionRequestDTO data, @PathVariable Long id){
        var transaction = transactionService.transactionCompleteUpdate(data, id);

        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        transactionService.deleteTransaction(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
