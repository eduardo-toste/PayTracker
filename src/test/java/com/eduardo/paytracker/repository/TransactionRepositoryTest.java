package com.eduardo.paytracker.repository;

import com.eduardo.paytracker.model.enums.TransactionType;
import com.eduardo.paytracker.model.Transaction;
import com.eduardo.paytracker.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    void setup() {
        user = new User(null, "Eduardo", "edu@email.com", "senha123");
        user = userRepository.save(user);

        Transaction t1 = new Transaction(null, "Aluguel", "Maio", new BigDecimal("1200"), LocalDate.now(),
                LocalDate.now().plusDays(5), TransactionType.EXPENSE, user);
        Transaction t2 = new Transaction(null, "Salário", "Abril", new BigDecimal("5000"), LocalDate.now(),
                LocalDate.now().plusDays(10), TransactionType.INCOME, user);

        transactionRepository.saveAll(List.of(t1, t2));
        entityManager.flush(); // garante que as queries sejam executadas
    }

    @Test
    void shouldFindAllTransactionsByUserId() {
        var page = transactionRepository.findAllTransactions(user.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void shouldFindTransactionByUserIdAndTransactionId() {
        Transaction saved = transactionRepository.findAll().get(0);

        Transaction result = transactionRepository.findTransactionById(user.getId(), saved.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(saved.getId());
    }

    @Test
    void shouldFindByDueDate() {
        LocalDate dueDate = LocalDate.now().plusDays(5);

        List<Transaction> result = transactionRepository.findByDueDate(dueDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Aluguel");
    }
}
