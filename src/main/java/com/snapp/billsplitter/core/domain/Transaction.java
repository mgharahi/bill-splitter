package com.snapp.billsplitter.core.domain;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public final class Transaction {
    private final String id;
    private final BigDecimal amount;
    private final SplitStrategy splitStrategy;
    private final Event event;
    private final Set<Owe> debts;
    private final User owner;

    @Builder
    private Transaction(String id, BigDecimal amount, SplitStrategy splitStrategy, Event event, Set<Owe> debts, User owner) {
        this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;

        this.amount = amount;
        this.splitStrategy = splitStrategy;
        this.event = event;
        this.debts = debts == null ? Collections.emptySet() : Collections.unmodifiableSet(debts);
        this.owner = owner;
    }

    private Transaction(BigDecimal amount, SplitStrategy splitStrategy, Event event, Set<Owe> debts, User owner) {
        this(null, amount, splitStrategy, event, debts, owner);
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