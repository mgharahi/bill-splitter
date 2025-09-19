package com.snapp.billsplitter.infrastructure.controller;

import com.snapp.billsplitter.application.service.transaction.TransactionService;
import com.snapp.billsplitter.application.service.transaction.dto.command.AddTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping
    public ResponseEntity<?> addTransaction(@RequestBody AddTransactionRequest request) {
        transactionService.addTransaction(request);
        return ResponseEntity.ok("success");
    }
}
