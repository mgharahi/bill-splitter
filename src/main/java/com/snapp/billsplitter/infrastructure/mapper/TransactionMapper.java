package com.snapp.billsplitter.infrastructure.mapper;

import com.snapp.billsplitter.core.domain.Transaction;
import com.snapp.billsplitter.infrastructure.spring.entity.TransactionEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "id", target = "id", qualifiedByName = "stringToLong")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "splitStrategy", target = "splitStrategy")
    @Mapping(source = "owner", target = "owner")
    @Mapping(source = "debts", target = "owes")
    TransactionEntity toTransaction(Transaction transaction);

    @AfterMapping
    default void linkTransaction(@MappingTarget TransactionEntity transactionEntity) {
        if (transactionEntity.getOwes() != null) {
            transactionEntity.getOwes().forEach(owe -> owe.setTransaction(transactionEntity));
        }
    }

    @Named("stringToLong")
    default Long stringToLong(String id) {
        if (id == null) return null;
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
