package com.snapp.billsplitter.core.domain.split.strategy;

import com.snapp.billsplitter.core.domain.User;
import com.snapp.billsplitter.core.domain.UserDebt;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class UnequalSplitStrategy implements SplitStrategyCalculator {

    @Override
    public Set<UserDebt> split(BigDecimal totalAmount, Set<UserDebt> participants) {
        return participants;
    }
}