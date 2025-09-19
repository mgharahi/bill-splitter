package com.snapp.billsplitter.core.domain.split.strategy;

import com.snapp.billsplitter.core.domain.User;
import com.snapp.billsplitter.core.domain.UserDebt;

import java.math.BigDecimal;
import java.util.Set;

public interface SplitStrategyCalculator {
    Set<UserDebt> split(BigDecimal totalAmount, Set<UserDebt> participants);
}
