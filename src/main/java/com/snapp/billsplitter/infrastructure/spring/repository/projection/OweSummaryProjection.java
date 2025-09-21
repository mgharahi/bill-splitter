package com.snapp.billsplitter.infrastructure.spring.repository.projection;

import java.math.BigDecimal;

public interface OweSummaryProjection {
    String getDebtor();
    String getCreditor();
    BigDecimal getAmount();
}