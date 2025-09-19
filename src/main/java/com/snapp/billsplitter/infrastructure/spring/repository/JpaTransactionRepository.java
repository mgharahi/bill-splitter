package com.snapp.billsplitter.infrastructure.spring.repository;

import com.snapp.billsplitter.infrastructure.spring.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, Long> {

}