package com.snapp.billsplitter.application.service.transaction.dto.command;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public record AddTransactionRequest(Long eventId, BigDecimal transactionAmount, SplitStrategy splitStrategy,
                                    Map<Long, BigDecimal> participants) {

}