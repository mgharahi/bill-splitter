package com.snapp.billsplitter.infrastructure.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "owe_summary",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"debtor_id", "creditor_id"})
        }
)
@Getter
@Setter
public class OweSummaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity debtor;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity creditor;
}