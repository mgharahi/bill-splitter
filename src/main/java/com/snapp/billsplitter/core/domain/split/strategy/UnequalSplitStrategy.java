package com.snapp.billsplitter.core.domain.split.strategy;

import com.snapp.billsplitter.core.domain.User;
import com.snapp.billsplitter.core.domain.Owe;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class UnequalSplitStrategy implements SplitStrategyCalculator {

    @Override
    public Set<Owe> split(BigDecimal totalAmount, Set<Owe> participants, User creditor) {
        return participants;
    }

    @Override
    public Boolean validate(Set<Owe> participants, BigDecimal totalAmount) {
        return totalAmount.equals(BigDecimal.valueOf(participants.stream().mapToLong(x -> x.getAmount().longValue()).sum()));
    }
}