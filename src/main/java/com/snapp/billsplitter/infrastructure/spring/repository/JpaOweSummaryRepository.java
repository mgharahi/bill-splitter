package com.snapp.billsplitter.infrastructure.spring.repository;

import com.snapp.billsplitter.infrastructure.spring.entity.OweSummaryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface JpaOweSummaryRepository extends JpaRepository<OweSummaryEntity, Long> {
    Optional<OweSummaryEntity> findByDebtor_idAndCreditor_id(Long debtor_id, Long creditor_id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<OweSummaryEntity> findAndLockByDebtor_idAndCreditor_Id(Long debtor_id, Long creditor_id);
}