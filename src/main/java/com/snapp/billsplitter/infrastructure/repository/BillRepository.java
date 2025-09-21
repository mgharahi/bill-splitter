package com.snapp.billsplitter.infrastructure.repository;

import com.snapp.billsplitter.core.domain.Owe;
import com.snapp.billsplitter.core.domain.Transaction;
import com.snapp.billsplitter.core.domain.User;
import com.snapp.billsplitter.infrastructure.mapper.OweMapper;
import com.snapp.billsplitter.infrastructure.mapper.TransactionMapper;
import com.snapp.billsplitter.infrastructure.spring.entity.OweSummaryEntity;
import com.snapp.billsplitter.infrastructure.spring.entity.TransactionEntity;
import com.snapp.billsplitter.infrastructure.spring.entity.UserEntity;
import com.snapp.billsplitter.infrastructure.spring.repository.JpaOweSummaryRepository;
import com.snapp.billsplitter.infrastructure.spring.repository.JpaTransactionRepository;
import com.snapp.billsplitter.infrastructure.spring.repository.JpaUserRepository;
import com.snapp.billsplitter.infrastructure.spring.repository.projection.OweSummaryProjection;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BillRepository implements com.snapp.billsplitter.core.repository.BillRepository {

    private final JpaUserRepository jpaUserRepository;
    private final JpaOweSummaryRepository jpaOweSummaryRepository;
    private final TransactionMapper transactionMapper;
    private final JpaTransactionRepository jpaTransactionRepository;
    private final ApplicationContext applicationContext;
    private final OweMapper oweMapper;

    @Override
    public Set<User> findByEventId(String _eventId) {
        long eventId = Long.parseLong(_eventId);
        var users = jpaUserRepository.findByEventId(eventId);
        return users.stream().map(u -> User.builder()
                .id(u.toString())
                .build()).collect(Collectors.toSet());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void addNewTransaction(String _userId, Transaction transaction) {

        TransactionEntity transactionEntity = transactionMapper.toTransaction(transaction);

        jpaTransactionRepository.save(transactionEntity);

        for (Owe owe : transaction.getDebts()) {
            applicationContext.getBean(BillRepository.class).saveOweIfNotExist(owe);
            var oweSummary = jpaOweSummaryRepository.findAndLockByDebtor_idAndCreditor_Id(
                    Long.parseLong(owe.getDebtor().getId()),
                    Long.parseLong(owe.getCreditor().getId())
            ).orElseThrow(EntityNotFoundException::new);

            oweSummary.setAmount(oweSummary.getAmount().add(owe.getAmount()));
            jpaOweSummaryRepository.save(oweSummary);
        }
    }

    @Override
    public List<Owe> oweSummaryByUserId(String _userId) {
        long userId = Long.parseLong(_userId);
        List<OweSummaryProjection> _owes = jpaOweSummaryRepository.getOweSummariesByDebtorIdOrCreditorId(userId);
        return _owes.stream().map(oweMapper::oweSummaryProjectionToOwe).collect(Collectors.toList());
    }

    @Transactional
    public void saveOweIfNotExist(Owe owe) {
        jpaOweSummaryRepository.findByDebtor_idAndCreditor_id(Long.parseLong(owe.getDebtor().getId()),
                Long.parseLong(owe.getCreditor().getId())).or(() -> {
            UserEntity debtor = new UserEntity();
            UserEntity creditor = new UserEntity();
            debtor.setId(Long.parseLong(owe.getDebtor().getId()));
            creditor.setId(Long.parseLong(owe.getCreditor().getId()));
            OweSummaryEntity newSummary = new OweSummaryEntity();
            newSummary.setAmount(BigDecimal.ZERO);
            newSummary.setCreditor(creditor);
            newSummary.setDebtor(debtor);
            jpaOweSummaryRepository.save(newSummary);
            return java.util.Optional.empty();
        });
    }
}