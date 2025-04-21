package com.eduardo.paytracker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.eduardo.paytracker.dto.TransactionPatchRequestDTO;
import com.eduardo.paytracker.dto.TransactionRequestDTO;
import com.eduardo.paytracker.exception.TransactionNotFoundException;
import com.eduardo.paytracker.model.Transaction;
import com.eduardo.paytracker.model.User;
import com.eduardo.paytracker.model.enums.TransactionType;
import com.eduardo.paytracker.repository.TransactionRepository;
import com.eduardo.paytracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User fakeUser;
    private Transaction fakeTransaction;
    private Pageable pageable;

    @BeforeEach
    void setup() {
        fakeUser = new User(1L, "Eduardo", "edu@email.com", "senha123");
        fakeTransaction = new Transaction(1L, "Aluguel", "Maio", new BigDecimal(1200),
                LocalDate.of(2025, 4, 21), LocalDate.of(2025, 4, 26),
                TransactionType.EXPENSE, fakeUser);

        pageable = PageRequest.of(0, 10);
    }

    private void mockSecurityContext(User user) {
        var authentication = mock(Authentication.class);
        when(authentication.getCredentials()).thenReturn(generateTokenFor(user));
        var context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
    }

    private String generateTokenFor(User user) {
        return JWT.create().withClaim("sub", user.getEmail()).sign(Algorithm.HMAC256("test"));
    }

    @Test
    void givenValidDTO_whenCreateTransaction_thenSavesSuccessfully() {
        mockSecurityContext(fakeUser);

        TransactionRequestDTO dto = new TransactionRequestDTO(
                "Salário",
                "Recebimento",
                new BigDecimal(5000),
                LocalDate.of(2025, 4, 26),
                TransactionType.INCOME
        );

        when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(fakeUser);
        when(userRepository.getReferenceById(fakeUser.getId())).thenReturn(fakeUser);

        Transaction transactionWithId = new Transaction(
                10L,
                dto.title(),
                dto.description(),
                dto.amount(),
                LocalDate.now(),
                dto.dueDate(),
                dto.type(),
                fakeUser
        );

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionWithId);

        var result = transactionService.createTransaction(dto);

        assertEquals("Salário", result.title());
        assertEquals(TransactionType.INCOME, result.type());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void givenUserWithTransactions_whenGetAll_thenReturnsPage() {
        mockSecurityContext(fakeUser);

        Page<Transaction> page = new PageImpl<>(List.of(fakeTransaction));
        when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(fakeUser);
        when(userRepository.getReferenceById(fakeUser.getId())).thenReturn(fakeUser);
        when(transactionRepository.findAllTransactions(fakeUser.getId(), pageable)).thenReturn(page);

        var result = transactionService.getAllTransactions(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Aluguel", result.getContent().get(0).title());
    }

    @Test
    void givenUserWithoutTransactions_whenGetAll_thenThrowsException() {
        mockSecurityContext(fakeUser);

        Page<Transaction> emptyPage = new PageImpl<>(List.of());
        when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(fakeUser);
        when(userRepository.getReferenceById(fakeUser.getId())).thenReturn(fakeUser);
        when(transactionRepository.findAllTransactions(fakeUser.getId(), pageable)).thenReturn(emptyPage);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getAllTransactions(pageable));
    }

    @Test
    void givenValidId_whenGetById_thenReturnsDTO() {
        mockSecurityContext(fakeUser);

        when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(fakeUser);
        when(userRepository.getReferenceById(fakeUser.getId())).thenReturn(fakeUser);
        when(transactionRepository.findTransactionById(fakeUser.getId(), 1L)).thenReturn(fakeTransaction);

        var result = transactionService.getTransactionById(1L);

        assertEquals("Aluguel", result.title());
        assertEquals(BigDecimal.valueOf(1200), result.amount());
    }

    @Test
    void givenInvalidId_whenGetById_thenThrowsException() {
        mockSecurityContext(fakeUser);

        when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(fakeUser);
        when(userRepository.getReferenceById(fakeUser.getId())).thenReturn(fakeUser);
        when(transactionRepository.findTransactionById(fakeUser.getId(), 1L)).thenReturn(null);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(1L));
    }

    @Test
    void givenValidId_whenSpecificUpdate_thenUpdatesFields() {
        mockSecurityContext(fakeUser);

        TransactionPatchRequestDTO patch = new TransactionPatchRequestDTO("Luz", null, null, null, null);
        when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(fakeUser);
        when(userRepository.getReferenceById(fakeUser.getId())).thenReturn(fakeUser);
        when(transactionRepository.findTransactionById(fakeUser.getId(), 1L)).thenReturn(fakeTransaction);

        var result = transactionService.transactionSpecificUpdate(patch, 1L);

        assertEquals("Luz", result.title());
        verify(transactionRepository).save(fakeTransaction);
    }

    @Test
    void givenInvalidId_whenSpecificUpdate_thenThrowsException() {
        mockSecurityContext(fakeUser);

        TransactionPatchRequestDTO patch = new TransactionPatchRequestDTO("Luz", null, null, null, null);
        when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(fakeUser);
        when(userRepository.getReferenceById(fakeUser.getId())).thenReturn(fakeUser);
        when(transactionRepository.findTransactionById(fakeUser.getId(), 1L)).thenReturn(null);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.transactionSpecificUpdate(patch, 1L));
    }

    @Test
    void givenValidId_whenCompleteUpdate_thenOverwritesAllFields() {
        mockSecurityContext(fakeUser);

        TransactionRequestDTO update = new TransactionRequestDTO("Internet", "Atualizado", new BigDecimal(200),
                LocalDate.of(2025, 4, 26), TransactionType.EXPENSE);

        when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(fakeUser);
        when(userRepository.getReferenceById(fakeUser.getId())).thenReturn(fakeUser);
        when(transactionRepository.findTransactionById(fakeUser.getId(), 1L)).thenReturn(fakeTransaction);

        var result = transactionService.transactionCompleteUpdate(update, 1L);

        assertEquals("Internet", result.title());
        assertEquals(BigDecimal.valueOf(200), result.amount());
        verify(transactionRepository).save(fakeTransaction);
    }

    @Test
    void givenInvalidId_whenCompleteUpdate_thenThrowsException() {
        mockSecurityContext(fakeUser);

        TransactionRequestDTO update = new TransactionRequestDTO("Internet", "Atualizado", new BigDecimal(200),
                LocalDate.of(2025, 4, 26), TransactionType.EXPENSE);

        when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(fakeUser);
        when(userRepository.getReferenceById(fakeUser.getId())).thenReturn(fakeUser);
        when(transactionRepository.findTransactionById(fakeUser.getId(), 1L)).thenReturn(null);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.transactionCompleteUpdate(update, 1L));
    }

    @Test
    void givenValidId_whenDelete_thenRemovesTransaction() {
        mockSecurityContext(fakeUser);

        when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(fakeUser);
        when(userRepository.getReferenceById(fakeUser.getId())).thenReturn(fakeUser);
        when(transactionRepository.findTransactionById(fakeUser.getId(), 1L)).thenReturn(fakeTransaction);

        transactionService.deleteTransaction(1L);

        verify(transactionRepository).delete(fakeTransaction);
    }

    @Test
    void givenInvalidId_whenDelete_thenThrowsException() {
        mockSecurityContext(fakeUser);

        when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(fakeUser);
        when(userRepository.getReferenceById(fakeUser.getId())).thenReturn(fakeUser);
        when(transactionRepository.findTransactionById(fakeUser.getId(), 1L)).thenReturn(null);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransaction(1L));
    }
}
