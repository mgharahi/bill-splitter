package com.snapp.billsplitter.infrastructure.spring.repository;

import com.snapp.billsplitter.infrastructure.spring.entity.OweSummaryEntity;
import com.snapp.billsplitter.infrastructure.spring.repository.projection.OweSummaryProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface JpaOweSummaryRepository extends JpaRepository<OweSummaryEntity, Long> {
    Optional<OweSummaryEntity> findByDebtor_idAndCreditor_id(Long debtor_id, Long creditor_id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<OweSummaryEntity> findAndLockByDebtor_idAndCreditor_Id(Long debtor_id, Long creditor_id);

    @Query(value = "WITH Balances AS (\n" +
            "    SELECT userId,\n" +
            "           SUM(credit) - SUM(debit) AS net_balance\n" +
            "    FROM (\n" +
            "             SELECT creditor_id AS userId, amount AS credit, 0 AS debit\n" +
            "             FROM owe_summary\n" +
            "             UNION ALL\n" +
            "             SELECT debtor_id, 0, amount\n" +
            "             FROM owe_summary\n" +
            "         ) t\n" +
            "    GROUP BY userId\n" +
            ")\n" +
            "SELECT net_balance \n" +
            "FROM Balances\n" +
            "where userId = :userId", nativeQuery = true)
    BigDecimal getNetBalance(@Param("userId") Long userId);

    @Query(value = "select ud.username debtor,uc.username creditor,os.amount\n" +
            "from owe_summary os\n" +
            "inner join users ud on ud.id = os.debtor_id\n" +
            "inner join users uc on uc.id = os.creditor_id\n" +
            "where os.creditor_id = :userId or os.debtor_id = :userId",nativeQuery = true)
    List<OweSummaryProjection> getOweSummariesByDebtorIdOrCreditorId(Long userId);
}