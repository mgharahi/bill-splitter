package com.snapp.billsplitter.core.domain.split.strategy;

import com.snapp.billsplitter.core.domain.Owe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

public class PercentageSplitStrategy implements SplitStrategyCalculator {

    @Override
    public Set<Owe> split(BigDecimal totalAmount, Set<Owe> participants) {
        Set<Owe> debts = new HashSet<>();
        for (Owe user : participants) {
            BigDecimal percent = user.getAmount();
            BigDecimal amount = totalAmount
                    .multiply(percent)
                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            debts.add(new Owe(user.getUser(), amount));
        }
        return debts;
    }
}