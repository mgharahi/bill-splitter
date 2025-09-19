package com.snapp.billsplitter.core.repository;

import com.snapp.billsplitter.core.domain.Transaction;
import com.snapp.billsplitter.core.domain.User;

import java.util.Set;

public interface UserRepository {
    Set<User> findByEventId(String eventId);

    void addNewTransaction(String userId, Transaction transaction);

}
