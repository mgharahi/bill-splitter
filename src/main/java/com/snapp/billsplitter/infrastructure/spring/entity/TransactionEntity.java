package com.snapp.billsplitter.infrastructure.spring.entity;

import com.snapp.billsplitter.infrastructure.spring.entity._enum.SplitStrategy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;


@Entity
@Table(name = "transactions")
@Getter
@Setter
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity owner;

    @Column(name = "split_strategy")
    @Enumerated(EnumType.STRING)
    private SplitStrategy splitStrategy;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OweEntity> owes;
}