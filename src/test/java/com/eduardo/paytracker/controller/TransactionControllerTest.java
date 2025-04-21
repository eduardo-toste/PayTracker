package com.eduardo.paytracker.controller;

import com.eduardo.paytracker.dto.TransactionPatchRequestDTO;
import com.eduardo.paytracker.dto.TransactionRequestDTO;
import com.eduardo.paytracker.dto.TransactionResponseDTO;
import com.eduardo.paytracker.model.enums.TransactionType;
import com.eduardo.paytracker.exception.TransactionNotFoundException;
import com.eduardo.paytracker.repository.UserRepository;
import com.eduardo.paytracker.service.TokenService;
import com.eduardo.paytracker.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(TransactionControllerTest.MockBeans.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @TestConfiguration
    static class MockBeans {
        @Bean
        TransactionService transactionService() {
            return mock(TransactionService.class);
        }

        @Bean
        TokenService tokenService() {
            return mock(TokenService.class);
        }

        @Bean
        UserRepository userRepository() {
            return mock(UserRepository.class);
        }
    }

    @jakarta.annotation.Resource
    private MockMvc mockMvc;

    @jakarta.annotation.Resource
    private TransactionService transactionService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    LocalDate hoje = LocalDate.of(2025, 4, 21);
    LocalDate vencimento = LocalDate.of(2025, 4, 30);

    @Test
    void givenValidData_whenCreateTransaction_thenReturnsCreated() throws Exception {
        TransactionRequestDTO request = new TransactionRequestDTO(
                "Salário", "Recebimento mensal", new BigDecimal("3500.00"),
                vencimento, TransactionType.INCOME
        );

        TransactionResponseDTO response = new TransactionResponseDTO(
                1L, "Salário", "Recebimento mensal", new BigDecimal("3500.00"),
                hoje, vencimento, TransactionType.INCOME, 1L
        );

        when(transactionService.createTransaction(any())).thenReturn(response);

        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Salário")))
                .andExpect(jsonPath("$.type", is("INCOME")))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void givenExistingTransactions_whenGetAll_thenReturnsList() throws Exception {
        TransactionResponseDTO response = new TransactionResponseDTO(
                1L, "Aluguel", "Maio", new BigDecimal("1200.00"),
                hoje, vencimento, TransactionType.EXPENSE, 1L
        );

        when(transactionService.getAllTransactions(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/transaction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", is("Aluguel")))
                .andExpect(jsonPath("$.content[0].userId", is(1)));
    }

    @Test
    void givenValidId_whenGetById_thenReturnsTransaction() throws Exception {
        TransactionResponseDTO response = new TransactionResponseDTO(
                1L, "Internet", "Plano mensal", new BigDecimal("150.00"),
                hoje, vencimento, TransactionType.EXPENSE, 1L
        );

        when(transactionService.getTransactionById(1L)).thenReturn(response);

        mockMvc.perform(get("/transaction/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Internet")))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void givenInvalidId_whenGetById_thenReturnsNotFound() throws Exception {
        when(transactionService.getTransactionById(99L))
                .thenThrow(new TransactionNotFoundException("Transaction not found"));

        mockMvc.perform(get("/transaction/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenPatchData_whenSpecificUpdate_thenReturnsUpdatedTransaction() throws Exception {
        TransactionPatchRequestDTO patch = new TransactionPatchRequestDTO("Luz", "Atualizado", null, null, null);

        TransactionResponseDTO response = new TransactionResponseDTO(
                1L, "Luz", "Atualizado", new BigDecimal("120.00"),
                hoje, vencimento, TransactionType.EXPENSE, 1L
        );

        when(transactionService.transactionSpecificUpdate(any(), eq(1L))).thenReturn(response);

        mockMvc.perform(patch("/transaction/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Luz")))
                .andExpect(jsonPath("$.description", is("Atualizado")))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void givenFullData_whenCompleteUpdate_thenReturnsUpdatedTransaction() throws Exception {
        TransactionRequestDTO update = new TransactionRequestDTO(
                "Plano de Saúde", "Bradesco", new BigDecimal("450.00"),
                vencimento, TransactionType.EXPENSE
        );

        TransactionResponseDTO response = new TransactionResponseDTO(
                1L, "Plano de Saúde", "Bradesco", new BigDecimal("450.00"),
                hoje, vencimento, TransactionType.EXPENSE, 1L
        );

        when(transactionService.transactionCompleteUpdate(any(), eq(1L))).thenReturn(response);

        mockMvc.perform(put("/transaction/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Plano de Saúde")))
                .andExpect(jsonPath("$.description", is("Bradesco")))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void givenValidId_whenDeleteTransaction_thenReturnsNoContent() throws Exception {
        doNothing().when(transactionService).deleteTransaction(1L);

        mockMvc.perform(delete("/transaction/1"))
                .andExpect(status().isNoContent());

        verify(transactionService).deleteTransaction(1L);
    }
}
