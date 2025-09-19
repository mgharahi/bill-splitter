package com.snapp.billsplitter.infrastructure.mapper;

import com.snapp.billsplitter.core.domain.Owe;
import com.snapp.billsplitter.infrastructure.spring.entity.OweEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OweMapper {

    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "debtor", target = "debtor")
    @Mapping(source = "creditor", target = "creditor")
    OweEntity toOwe(Owe owe);

}
