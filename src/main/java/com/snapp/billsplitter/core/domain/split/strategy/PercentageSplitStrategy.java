package com.snapp.billsplitter.core.domain.split.strategy;

import com.snapp.billsplitter.core.domain.Owe;
import com.snapp.billsplitter.core.domain.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

public class PercentageSplitStrategy implements SplitStrategyCalculator {

    @Override
    public Set<Owe> split(BigDecimal totalAmount, Set<Owe> participants, User creator) {
        Set<Owe> debts = new HashSet<>();
        for (Owe user : participants) {
            BigDecimal percent = user.getAmount();
            BigDecimal amount = totalAmount
                    .multiply(percent)
                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

            debts.add(Owe.builder()
                    .debtor(user.getDebtor())
                    .amount(amount)
                            .creditor(creator)
                    .build());

        }
        return debts;
    }

    @Override
    public Boolean validate(Set<Owe> participants, BigDecimal totalAmount) {
        return participants.stream().mapToLong(x -> x.getAmount().longValue()).sum() == 100L;
    }
}