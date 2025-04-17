package com.eduardo.paytracker.repository;

import com.eduardo.paytracker.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.user.id = :userId")
    Page<Transaction> findAllTransactions(Long userId, Pageable pageable);

    @Query("select t from Transaction t where t.id = :transactionId and t.user.id = :userId")
    Transaction findTransactionById(Long userId, Long transactionId);

    List<Transaction> findByDueDate(LocalDate dueDate);

}
