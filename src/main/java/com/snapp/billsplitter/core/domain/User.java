package com.snapp.billsplitter.core.domain;

import com.snapp.billsplitter.core.domain.split.strategy.SplitStrategyCalculator;
import com.snapp.billsplitter.core.domain.split.strategy.SplitStrategyFactory;
import com.sun.java.accessibility.util.GUIInitializedListener;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public final class User {
    private final String id;
    private final Set<Transaction> transactions;
    private final Set<Event> events;


    public User(String id, Set<Transaction> transactions, Set<Event> events) {
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
        this.id = id;
        this.transactions = Collections.unmodifiableSet(transactions);
        this.events = Collections.unmodifiableSet(events);
    }

    public User addTransaction(Transaction transaction, Set<UserDebt> debtors) {
        throwExceptionIfAddTransactionInputIsInvalid(transaction, debtors);

        Set<UserDebt> calculatedDebtors = SplitStrategyFactory.getCalculator(transaction.getStrategy()).split(transaction.getAmount(), debtors);
        Set<Owe> owes = calculatedDebtors
                .stream()
                .map(x -> new Owe(x.getUser(), x.getAmount()))
                .collect(Collectors.toSet());
        Transaction newTransaction = new Transaction(transaction.getAmount(), transaction.getStrategy(), transaction.getEvent(), owes);
        Set<Transaction> newTransactions = new HashSet<>(transactions);
        newTransactions.add(newTransaction);

        return new User(id, newTransactions, events);
    }

    private void throwExceptionIfAddTransactionInputIsInvalid(Transaction transaction, Set<UserDebt> debtors) {
        if (Objects.isNull(transaction)) {
            throw new IllegalArgumentException("transaction cannot be null");
        }
        if (Objects.isNull(debtors) || debtors.isEmpty()) {
            throw new IllegalArgumentException("debtors cannot be empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}