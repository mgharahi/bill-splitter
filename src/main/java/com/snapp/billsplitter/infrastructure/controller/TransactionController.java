package com.snapp.billsplitter.infrastructure.controller;

import com.snapp.billsplitter.application.service.transaction.TransactionService;
import com.snapp.billsplitter.application.service.transaction.dto.command.AddTransactionRequest;
import com.snapp.billsplitter.infrastructure.controller.dto.auth.AuthRequest;
import com.snapp.billsplitter.infrastructure.service.bucket4j.RateLimitService;
import com.snapp.billsplitter.infrastructure.util.CurrentUser;
import com.snapp.billsplitter.infrastructure.util.MessageHelper;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final MessageHelper messageHelper;
    private final TransactionService transactionService;
    private final RateLimitService rateLimitService;
    private final CurrentUser currentUser;

    @Operation(
            summary = "Add new transaction",
            description = "Add new transaction to an existed event with some participants",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "The transaction detail to save as new transaction",
                    content = @Content(schema = @Schema(implementation = AddTransactionRequest.class))
            )
    )
    @PutMapping
    public ResponseEntity<?> addTransaction(@RequestBody AddTransactionRequest request) {
        transactionService.addTransaction(request);
        return ResponseEntity.ok("success");
    }


    @Operation(
            summary = "Get user balance",
            description = "Get user balance as pdf file"
    )
    @GetMapping("/report/balance")
    public ResponseEntity<byte[]> getBalanceReport() {

        Bucket bucket = rateLimitService.resolveBucket(currentUser.getUserId());
        if (!bucket.tryConsume(1))
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(messageHelper.getMessage("error.too.many.request").getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=SummaryReport.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(transactionService.getOweSummaryBalanceReport(currentUser.getUserId()));
    }
}