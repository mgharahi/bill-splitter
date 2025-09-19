package com.snapp.billsplitter.application.service.transaction;

import com.snapp.billsplitter.application.service.transaction.dto.command.AddTransactionRequest;
import com.snapp.billsplitter.core.domain.*;
import com.snapp.billsplitter.core.repository.UserRepository;
import com.snapp.billsplitter.infrastructure.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final CurrentUser currentUser;
    private final UserRepository userRepository;

    public void addTransaction(AddTransactionRequest request) {
        User user = User.builder()
                .id(currentUser.getUserId().toString())
                .userRepository(userRepository)
                .build();

        Set<Owe> owes = request.participants().keySet().stream()
                .map(k -> Owe.builder()
                        .amount(request.participants().get(k))
                        .debtor(User.builder()
                                .id(k.toString())
                                .build())
                        .build()).collect(Collectors.toSet());

        user.addTransaction(Transaction.builder()
                .owner(user)
                .amount(request.transactionAmount())
                .event(Event.builder()
                        .id(request.eventId().toString())
                        .build())
                .splitStrategy(SplitStrategy.valueOf(request.splitStrategy().toString()))
                .build(), owes);
    }
}