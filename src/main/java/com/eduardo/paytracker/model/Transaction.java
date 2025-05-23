package com.eduardo.paytracker.model;

import com.eduardo.paytracker.dto.TransactionPatchRequestDTO;
import com.eduardo.paytracker.dto.TransactionRequestDTO;
import com.eduardo.paytracker.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "Transaction")
@Table(name = "transactions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Setter
    private String title;

    @Column(nullable = false)
    @Setter
    private String description;

    @Column(nullable = false)
    @Setter
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "due_date", nullable = false)
    @Setter
    private LocalDate dueDate;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void updateFrom(TransactionRequestDTO data) {
        this.title = data.title();
        this.description = data.description();
        this.amount = data.amount();
        this.dueDate = data.dueDate();
        this.type = data.type();
    }

    public void updateFromPatch(TransactionPatchRequestDTO data) {
        if (data.title() != null) this.title = data.title();
        if (data.description() != null) this.description = data.description();
        if (data.amount() != null) this.amount = data.amount();
        if (data.dueDate() != null) this.dueDate = data.dueDate();
        if (data.type() != null) this.type = data.type();
    }

}
