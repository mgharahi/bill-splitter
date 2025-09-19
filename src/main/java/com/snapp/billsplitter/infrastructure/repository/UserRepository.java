package com.snapp.billsplitter.infrastructure.repository;

import com.snapp.billsplitter.core.domain.Transaction;
import com.snapp.billsplitter.core.domain.User;
import com.snapp.billsplitter.infrastructure.exception.NotFoundException;
import com.snapp.billsplitter.infrastructure.mapper.TransactionMapper;
import com.snapp.billsplitter.infrastructure.service.messages.MessageHelper;
import com.snapp.billsplitter.infrastructure.spring.entity.TransactionEntity;
import com.snapp.billsplitter.infrastructure.spring.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserRepository implements com.snapp.billsplitter.core.repository.UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final MessageHelper messageHelper;
    private final TransactionMapper transactionMapper;

    @Override
    public Set<User> findByEventId(String _eventId) {
        long eventId = Long.parseLong(_eventId);
        var users = jpaUserRepository.findByEventId(eventId);
        return users.stream().map(u -> User.builder()
                .id(u.toString())
                .build()).collect(Collectors.toSet());
    }

    @Override
    public void addNewTransaction(String _userId, Transaction transaction) {
        Long userId = Long.parseLong(_userId);
        var user = jpaUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage("error.user.not_found", new Object[]{userId})
                        )
                );

        TransactionEntity transactionEntity = transactionMapper.toTransaction(transaction);

        user.getTransactions().add(transactionEntity);
        jpaUserRepository.save(user);
    }
}