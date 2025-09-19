package com.snapp.billsplitter.core.domain.split.strategy;

import com.snapp.billsplitter.core.domain.UserDebt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

public class PercentageSplitStrategy implements SplitStrategyCalculator {

    @Override
    public Set<UserDebt> split(BigDecimal totalAmount, Set<UserDebt> participants) {
        Set<UserDebt> debts = new HashSet<>();
        for (UserDebt user : participants) {
            BigDecimal percent = user.getAmount();
            BigDecimal amount = totalAmount
                    .multiply(percent)
                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            debts.add(new UserDebt(user.getUser(), amount));
        }
        return debts;
    }
}