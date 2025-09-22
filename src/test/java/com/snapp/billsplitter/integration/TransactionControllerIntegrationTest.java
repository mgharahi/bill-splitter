package com.snapp.billsplitter.integration;

import com.snapp.billsplitter.application.service.transaction.dto.command.AddTransactionRequest;
import com.snapp.billsplitter.application.service.transaction.dto.command.SplitStrategy;
import com.snapp.billsplitter.infrastructure.spring.entity.TransactionEntity;
import com.snapp.billsplitter.infrastructure.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import com.snapp.billsplitter.infrastructure.spring.repository.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JpaTransactionRepository transactionRepository;

    @Test
    @WithMockUser(username = "testuser")
    void testAddTransactionEndpoint() throws Exception {
        String accessToken = jwtUtil.generateAccessToken(1L, "mehdi");

        mockMvc.perform(put("/transaction")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"eventId\":1,\"transactionAmount\":100,\"splitStrategy\":\"EQUALS\",\"participants\":{\"2\":50,\"3\":50}}"))
                .andExpect(status().isOk());

        List<TransactionEntity> transactions = transactionRepository.findAll();
        assertEquals(1, transactions.size());
        assertEquals(BigDecimal.valueOf(100), transactions.getFirst().getAmount());

    }
}