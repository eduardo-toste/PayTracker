package com.eduardo.paytracker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eduardo.paytracker.dto.TransactionRequestDTO;
import com.eduardo.paytracker.dto.TransactionResponseDTO;
import com.eduardo.paytracker.dto.TransactionPatchRequestDTO;
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

    public TransactionResponseDTO getTransactionById(Long transactionId) {
        var userId = getUser().getId();
        var transaction = transactionRepository.findTransactionById(userId, transactionId);

        if(transaction == null){
            throw new TransactionNotFoundException("Transaction not found!");
        }

        return new TransactionResponseDTO(transaction);
    }

    public TransactionResponseDTO updateSpecificTransaction(TransactionPatchRequestDTO data, Long id) {
        var userId = getUser().getId();
        var transaction = transactionRepository.findTransactionById(userId, id);

        if(transaction == null){
            throw new TransactionNotFoundException("Transaction not found!");
        }

        if (data.title() != null) {
            transaction.setTitle(data.title());
        }
        if (data.description() != null) {
            transaction.setDescription(data.description());
        }
        if (data.amount() != null) {
            transaction.setAmount(data.amount());
        }
        if (data.dueDate() != null) {
            transaction.setDueDate(data.dueDate());
        }
        if (data.type() != null) {
            transaction.setType(data.type());
        }

        transactionRepository.save(transaction);

        return new TransactionResponseDTO(transaction);
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
