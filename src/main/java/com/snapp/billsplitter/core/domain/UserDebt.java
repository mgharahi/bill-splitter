package com.snapp.billsplitter.core.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public final class UserDebt {
    private final User user;
    private final BigDecimal amount;

    public UserDebt(User user, BigDecimal amount) {
        this.user = user;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDebt userDebt = (UserDebt) o;
        return Objects.equals(user, userDebt.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }
}
