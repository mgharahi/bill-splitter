package com.snapp.billsplitter.core.domain;

import com.snapp.billsplitter.core.domain.split.strategy.SplitStrategyFactory;
import com.snapp.billsplitter.core.repository.UserRepository;
import lombok.Builder;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Builder
public final class User {
    private final String id;
    private final Set<Transaction> transactions;
    private final Set<Event> events;
    private final UserRepository userRepository;

    @Builder
    private User(String id,
                 Set<Transaction> transactions,
                 Set<Event> events,
                 UserRepository userRepository) {
        this.id = id == null || id.isBlank() ? UUID.randomUUID().toString() : id;
        this.transactions = transactions == null ? Set.of() : Collections.unmodifiableSet(transactions);
        this.events = events == null ? Set.of() : Collections.unmodifiableSet(events);
        this.userRepository = userRepository;
    }

    public User addTransaction(Transaction transaction, Set<Owe> debtors) {
        throwExceptionIfAddTransactionInputIsInvalid(transaction, debtors);
        throwExceptionIfAnyOfDebtorsDoesNotMemberOdEvent(transaction.getEvent(), debtors.stream().map(Owe::getUser).collect(Collectors.toSet()));

        Set<Owe> calculatedDebtors = SplitStrategyFactory.getCalculator(transaction.getStrategy()).split(transaction.getAmount(), debtors);
        Set<Owe> owes = calculatedDebtors
                .stream()
                .map(x -> new Owe(x.getUser(), x.getAmount()))
                .collect(Collectors.toSet());
        Transaction newTransaction = new Transaction(transaction.getAmount(), transaction.getStrategy(), transaction.getEvent(), owes);
        Set<Transaction> newTransactions = new HashSet<>(transactions);
        newTransactions.add(newTransaction);

        return User.builder()
                .id(id)
                .transactions(newTransactions)
                .events(events)
                .userRepository(userRepository)
                .build();
    }

    private void throwExceptionIfAnyOfDebtorsDoesNotMemberOdEvent(Event event, Set<User> debtors) {
        Set<User> eventUsers = userRepository.findByEventId(event.getId());
        if (debtors.stream().anyMatch(debtor -> !eventUsers.contains(debtor)))
            throw new IllegalArgumentException("The debtor does not belong to the event " + event.getId());
    }

    private void throwExceptionIfAddTransactionInputIsInvalid(Transaction transaction, Set<Owe> debtors) {
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