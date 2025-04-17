package com.eduardo.paytracker.scheduler;

import com.eduardo.paytracker.model.Transaction;
import com.eduardo.paytracker.repository.TransactionRepository;
import com.eduardo.paytracker.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final TransactionRepository transactionRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 22 * * *")
    public void checkExpiringTransactions() {
        LocalDate date = LocalDate.now().plusDays(2);
        List<Transaction> transactions = transactionRepository.findByDueDate(date);

        for (Transaction transaction : transactions) {
            emailService.sendEmail(transaction);
        }
    }

}
