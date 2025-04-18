package com.eduardo.paytracker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eduardo.paytracker.dto.TransactionPatchRequestDTO;
import com.eduardo.paytracker.dto.TransactionRequestDTO;
import com.eduardo.paytracker.dto.TransactionResponseDTO;
import com.eduardo.paytracker.exception.TransactionNotFoundException;
import com.eduardo.paytracker.model.Transaction;
import com.eduardo.paytracker.model.User;
import com.eduardo.paytracker.repository.TransactionRepository;
import com.eduardo.paytracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionResponseDTO createTransaction(TransactionRequestDTO data) {
        var user = getUser();

        var transaction = new Transaction(
                null,
                data.title(),
                data.description(),
                data.amount(),
                LocalDate.now(),
                data.dueDate(),
                data.type(),
                user
        );

        return new TransactionResponseDTO(transactionRepository.save(transaction));
    }

    public Page<TransactionResponseDTO> getAllTransactions(Pageable pageable) {
        var userId = getUser().getId();
        var transactions = transactionRepository.findAllTransactions(userId, pageable);

        if(transactions.isEmpty()){
            throw new TransactionNotFoundException("You haven't registered any transactions yet!");
        }

        return transactionRepository.findAllTransactions(userId, pageable).map(TransactionResponseDTO::new);
    }

    public TransactionResponseDTO getTransactionById(Long id) {
        var transaction = getTransactionOrThrow(id);

        return new TransactionResponseDTO(transaction);
    }

    public TransactionResponseDTO transactionSpecificUpdate(TransactionPatchRequestDTO data, Long id) {
        var transaction = getTransactionOrThrow(id);

        transaction.updateFromPatch(data);
        transactionRepository.save(transaction);

        return new TransactionResponseDTO(transaction);
    }

    public TransactionResponseDTO transactionCompleteUpdate(TransactionRequestDTO data, Long id) {
        var transaction = getTransactionOrThrow(id);

        transaction.updateFrom(data);
        transactionRepository.save(transaction);

        return new TransactionResponseDTO(transaction);
    }

    public void deleteTransaction(Long id) {
        var transaction = getTransactionOrThrow(id);
        transactionRepository.delete(transaction);
    }

    private Transaction getTransactionOrThrow(Long transactionId){
        var userId = getUser().getId();
        var transaction = transactionRepository.findTransactionById(userId, transactionId);

        if(transaction == null){
            throw new TransactionNotFoundException("Transaction not found!");
        }

        return transaction;
    }

    private User getUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        String token = (String) authentication.getCredentials();
        DecodedJWT decodedJWT = JWT.decode(token);
        String email = decodedJWT.getClaim("sub").asString();

        User user = (User) userRepository.findByEmail(email);
        Long userId = user.getId();

        return userRepository.getReferenceById(userId);
    }
}
