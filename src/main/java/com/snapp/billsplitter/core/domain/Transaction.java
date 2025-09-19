package com.snapp.billsplitter.core.domain;


import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
public final class Transaction {
    private final String id;
    private final BigDecimal amount;
    private final SplitStrategy strategy;
    private final Event event;
    private final Set<Owe> debts;


    public Transaction(String id, BigDecimal amount, SplitStrategy strategy, Event event, Set<Owe> debts) {
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
        this.id = id;
        this.amount = amount;
        this.strategy = strategy;
        this.event = event;
        this.debts = Collections.unmodifiableSet(debts);
    }

    public Transaction(BigDecimal amount, SplitStrategy strategy, Event event, Set<Owe> debts) {
        this(null,amount,strategy,event,debts);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}