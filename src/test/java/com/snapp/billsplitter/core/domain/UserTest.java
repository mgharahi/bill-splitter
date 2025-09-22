package com.snapp.billsplitter.core.domain;

import com.snapp.billsplitter.core.domain.split.strategy.EqualSplitStrategy;
import com.snapp.billsplitter.core.domain.split.strategy.PercentageSplitStrategy;
import com.snapp.billsplitter.core.domain.split.strategy.SplitStrategyFactory;
import com.snapp.billsplitter.core.domain.split.strategy.UnequalSplitStrategy;
import com.snapp.billsplitter.core.repository.BillRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    private Event getSimpleEvent() {
        return Event.builder().id("event-1").build();
    }

    private List<User> getSomeSampleUsers(BillRepository repo) {
        return List.of(
                User.builder().id("u1").username("owner").billRepository(repo).events(Set.of(getSimpleEvent())).build()
                , User.builder().id("u2").username("debtor1").billRepository(repo).events(Set.of(getSimpleEvent())).build()
                , User.builder().id("u3").username("debtor2").billRepository(repo).events(Set.of(getSimpleEvent())).build()
                , User.builder().id("u4").username("debtor3").billRepository(repo).events(Set.of(getSimpleEvent())).build()
                , User.builder().id("u5").username("debtor4").billRepository(repo).events(Set.of(getSimpleEvent())).build()
        );
    }

    private Set<Owe> getSomeSampleOwes(List<User> users, BigDecimal amount) {
        return users
                .stream()
                .map(u -> Owe.builder().debtor(u).creditor(users.getFirst()).amount(amount).build())
                .collect(Collectors.toSet());
    }

    private Transaction getSimpleTransaction(User user, Set<Owe> owes, SplitStrategy splitStrategy) {
        return Transaction.builder()
                .event(getSimpleEvent())
                .owner(user)
                .amount(BigDecimal.valueOf(100))
                .splitStrategy(splitStrategy)
                .debts(owes)
                .build();
    }

    @Test
    void addTransaction_ShouldAdd_WithEqualsStrategy_WhenValidInput() {
        BillRepository repo = mock(BillRepository.class);

        var users = getSomeSampleUsers(repo);
        var owes = getSomeSampleOwes(users.subList(1, users.size()), BigDecimal.ZERO);
        var trx = getSimpleTransaction(users.getFirst(), owes, SplitStrategy.EQUALS);

        when(repo.findByEventId(getSimpleEvent().getId())).thenReturn(new HashSet<>(users));

        User updated = users.getFirst().addTransaction(trx);

        assertEquals(1, updated.getTransactions().size());
        assertEquals(4, updated.getTransactions().stream().findFirst().get().getDebts().size());
        verify(repo).addNewTransaction(eq("u1"), any(Transaction.class));
    }

    @Test
    void addTransaction_ShouldAdd_WithPercentStrategy_WhenValidInput() {
        BillRepository repo = mock(BillRepository.class);

        var users = getSomeSampleUsers(repo);
        var owes = getSomeSampleOwes(users.subList(1, users.size()), BigDecimal.valueOf(25));
        var trx = getSimpleTransaction(users.getFirst(), owes, SplitStrategy.PERCENTAGE);

        when(repo.findByEventId(getSimpleEvent().getId())).thenReturn(new HashSet<>(users));

        User updated = users.getFirst().addTransaction(trx);

        assertEquals(1, updated.getTransactions().size());
        assertEquals(4, updated.getTransactions().stream().findFirst().get().getDebts().size());
        assertEquals(updated.getTransactions().stream().findFirst().get().getDebts().stream().findFirst().get().getAmount(), BigDecimal.valueOf(25));
    }

    @Test
    void addTransaction_ShouldThrow_WhenSumOfOweUnequalsWithTransactionAmount() {
        BillRepository repo = mock(BillRepository.class);

        var users = getSomeSampleUsers(repo);
        Owe owe1 = Owe.builder().debtor(users.get(1)).amount(BigDecimal.valueOf(100)).build();
        Owe owe2 = Owe.builder().debtor(users.get(2)).amount(BigDecimal.valueOf(300)).build();
        Owe owe3 = Owe.builder().debtor(users.get(3)).amount(BigDecimal.valueOf(500)).build();
        var trx = getSimpleTransaction(users.getFirst(), Set.of(owe1, owe2, owe3), SplitStrategy.UNEQUALS);

        when(repo.findByEventId(getSimpleEvent().getId())).thenReturn(new HashSet<>(users));

        assertThrows(IllegalArgumentException.class,
                () -> users.getFirst().addTransaction(trx));
    }

    @Test
    void addTransaction_ShouldThrow_WhenDebtorNotInEvent() {
        BillRepository repo = mock(BillRepository.class);
        Event event = Event.builder().id("event-1").build();

        User owner = User.builder().id("u1").username("owner").billRepository(repo).events(Set.of(event)).build();
        User debtor = User.builder().id("u2").username("debtor").billRepository(repo).events(Set.of()).build();

        Owe owe = Owe.builder().debtor(debtor).creditor(owner).amount(BigDecimal.ZERO).build();
        Transaction tx = getSimpleTransaction(owner, Set.of(owe), SplitStrategy.EQUALS);

        when(repo.findByEventId(event.getId())).thenReturn(Set.of(owner));

        assertThrows(IllegalArgumentException.class,
                () -> owner.addTransaction(tx));
    }

    @Test
    void getCalculator_ShouldReturnCorrectStrategy() {
        assertInstanceOf(EqualSplitStrategy.class, SplitStrategyFactory.getCalculator(SplitStrategy.EQUALS));
        assertInstanceOf(UnequalSplitStrategy.class, SplitStrategyFactory.getCalculator(SplitStrategy.UNEQUALS));
        assertInstanceOf(PercentageSplitStrategy.class, SplitStrategyFactory.getCalculator(SplitStrategy.PERCENTAGE));
    }
}
