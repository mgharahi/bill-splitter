package com.snapp.billsplitter.core.domain;

import com.snapp.billsplitter.core.domain.split.strategy.SplitStrategyFactory;
import com.snapp.billsplitter.core.repository.BillRepository;
import lombok.Builder;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Builder
public final class User {
    private final String id;
    private final String username;
    private final Set<Transaction> transactions;
    private final Set<Event> events;
    private final BillRepository billRepository;

    @Builder
    private User(String id, String username,
                 Set<Transaction> transactions,
                 Set<Event> events,
                 BillRepository billRepository) {
        this.id = id == null || id.isBlank() ? UUID.randomUUID().toString() : id;
        this.username = username;
        this.transactions = transactions == null ? Set.of() : Collections.unmodifiableSet(transactions);
        this.events = events == null ? Set.of() : Collections.unmodifiableSet(events);
        this.billRepository = billRepository;
    }

    public User addTransaction(Transaction transaction) {
        throwExceptionIfAddTransactionInputIsInvalid(transaction, transaction.getDebts());
        throwExceptionIfAnyOfDebtorsDoesNotMemberOdEvent(transaction.getEvent(), transaction.getDebts().stream().map(Owe::getDebtor).collect(Collectors.toSet()));

        Set<Owe> calculatedDebtors = SplitStrategyFactory.getCalculator(transaction.getSplitStrategy()).split(transaction.getAmount(), transaction.getDebts(), this);

        Transaction newTransaction = Transaction.builder()
                .event(transaction.getEvent())
                .debts(calculatedDebtors)
                .owner(transaction.getOwner())
                .amount(transaction.getAmount())
                .splitStrategy(transaction.getSplitStrategy())
                .build();
        Set<Transaction> newTransactions = new HashSet<>(transactions);
        newTransactions.add(newTransaction);

        billRepository.addNewTransaction(id, newTransaction);

        return User.builder()
                .id(id)
                .transactions(newTransactions)
                .events(events)
                .billRepository(billRepository)
                .build();
    }

    private void throwExceptionIfAnyOfDebtorsDoesNotMemberOdEvent(Event event, Set<User> debtors) {
        Set<User> eventUsers = billRepository.findByEventId(event.getId());
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
        if (!SplitStrategyFactory.getCalculator(transaction.getSplitStrategy()).validate(debtors, transaction.getAmount())) {
            throw new IllegalArgumentException("The sum of debtors is not valid");
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