package com.snapp.billsplitter.core.repository;

import com.snapp.billsplitter.core.domain.Owe;
import com.snapp.billsplitter.core.domain.Transaction;
import com.snapp.billsplitter.core.domain.User;

import java.util.List;
import java.util.Set;

public interface BillRepository {
    Set<User> findByEventId(String eventId);

    void addNewTransaction(String userId, Transaction transaction);

    List<Owe> oweSummaryByUserId(String userId);
}