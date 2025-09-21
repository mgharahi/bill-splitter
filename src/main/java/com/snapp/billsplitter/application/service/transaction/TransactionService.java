package com.snapp.billsplitter.application.service.transaction;

import com.snapp.billsplitter.application.service.transaction.dto.command.AddTransactionRequest;
import com.snapp.billsplitter.core.domain.*;
import com.snapp.billsplitter.core.repository.BillRepository;
import com.snapp.billsplitter.infrastructure.spring.repository.JpaOweSummaryRepository;
import com.snapp.billsplitter.infrastructure.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final CurrentUser currentUser;
    private final BillRepository billRepository;
    private final JpaOweSummaryRepository oweSummaryRepository;

    public void addTransaction(AddTransactionRequest request) {
        User user = User.builder()
                .id(currentUser.getUserId().toString())
                .billRepository(billRepository)
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

    public byte[] getOweSummaryBalanceReport(Long userId) {
        try {
            List<Owe> data = billRepository.oweSummaryByUserId(userId.toString());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

            InputStream reportStream = getClass().getResourceAsStream("/reports/Balance.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (
                Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}