package com.eduardo.paytracker.controller;

import com.eduardo.paytracker.dto.TransactionRequestDTO;
import com.eduardo.paytracker.dto.TransactionResponseDTO;
import com.eduardo.paytracker.dto.TransactionPatchRequestDTO;
import com.eduardo.paytracker.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Transações", description = "Gerenciamento de transações financeiras dos usuários")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Cria uma nova transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou tipo inválido (Enum)")
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid TransactionRequestDTO data) {
        var createdTransaction = transactionService.createTransaction(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @Operation(summary = "Lista todas as transações do usuário com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário ainda não possui transações")
    })
    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> getAll(Pageable pageable) {
        var transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }

    @Operation(summary = "Busca uma transação específica pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação encontrada"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(@PathVariable Long id) {
        var transaction = transactionService.getTransactionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @Operation(summary = "Atualiza parcialmente uma transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> specificUpdate(
            @RequestBody @Valid TransactionPatchRequestDTO data,
            @PathVariable Long id) {

        var transaction = transactionService.transactionSpecificUpdate(data, id);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @Operation(summary = "Atualiza completamente uma transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou tipo inválido"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> completeUpdate(
            @RequestBody @Valid TransactionRequestDTO data,
            @PathVariable Long id) {

        var transaction = transactionService.transactionCompleteUpdate(data, id);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @Operation(summary = "Remove uma transação pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transação excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}