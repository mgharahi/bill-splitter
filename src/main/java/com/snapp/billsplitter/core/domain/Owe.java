package com.snapp.billsplitter.core.domain;

import java.math.BigDecimal;

public final class Owe {

    private final User user;
    private final BigDecimal amount;


    public Owe(User user, BigDecimal amount) {
        this.user = user;
        this.amount = amount;
    }
}
