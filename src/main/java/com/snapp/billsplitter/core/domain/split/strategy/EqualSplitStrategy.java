package com.snapp.billsplitter.core.domain.split.strategy;

import com.snapp.billsplitter.core.domain.User;
import com.snapp.billsplitter.core.domain.Owe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

public class EqualSplitStrategy implements SplitStrategyCalculator {

    @Override
    public Set<Owe> split(BigDecimal totalAmount, Set<Owe> participants) {
        Set<Owe> debts = new HashSet<>();
        if (participants == null || participants.isEmpty()) {
            return debts;
        }

        BigDecimal share = totalAmount.divide(BigDecimal.valueOf(participants.size()), RoundingMode.HALF_UP);

        for (Owe userDebt : participants) {
            debts.add(new Owe(userDebt.getUser(), share));
        }
        return debts;
    }
}
