package com.snapp.billsplitter.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Builder
public final class Owe {

    private final User debtor;
    private final User creditor;
    private final BigDecimal amount;

    @Builder
    private Owe(User debtor, User creditor, BigDecimal amount) {
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Owe owe = (Owe) o;
        return Objects.equals(debtor, owe.debtor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(debtor);
    }
}
