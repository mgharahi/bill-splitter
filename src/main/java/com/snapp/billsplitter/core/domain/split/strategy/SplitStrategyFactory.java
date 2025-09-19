package com.snapp.billsplitter.core.domain.split.strategy;

import com.snapp.billsplitter.core.domain.SplitStrategy;

public class SplitStrategyFactory {

    private final static SplitStrategyCalculator equal = new EqualSplitStrategy();
    private final static SplitStrategyCalculator percentage = new PercentageSplitStrategy();
    private final static SplitStrategyCalculator unequal = new UnequalSplitStrategy();

    public static SplitStrategyCalculator getCalculator(SplitStrategy strategy) {
        return switch (strategy) {
            case EQUAL -> equal;
            case UNEQUALS -> unequal;
            case PERCENTAGE -> percentage;
        };
    }
}