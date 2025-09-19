package com.snapp.billsplitter.core.domain.split.strategy;

import com.snapp.billsplitter.core.domain.User;
import com.snapp.billsplitter.core.domain.UserDebt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

public class EqualSplitStrategy implements SplitStrategyCalculator {

    @Override
    public Set<UserDebt> split(BigDecimal totalAmount, Set<UserDebt> participants) {
        Set<UserDebt> debts = new HashSet<>();
        if (participants == null || participants.isEmpty()) {
            return debts;
        }

        BigDecimal share = totalAmount.divide(BigDecimal.valueOf(participants.size()), RoundingMode.HALF_UP);

        for (UserDebt userDebt : participants) {
            debts.add(new UserDebt(userDebt.getUser(), share));
        }
        return debts;
    }
}
