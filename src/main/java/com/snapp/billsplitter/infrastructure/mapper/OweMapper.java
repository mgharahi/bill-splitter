package com.snapp.billsplitter.infrastructure.mapper;

import com.snapp.billsplitter.core.domain.Owe;
import com.snapp.billsplitter.infrastructure.spring.entity.OweEntity;
import com.snapp.billsplitter.infrastructure.spring.repository.projection.OweSummaryProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OweMapper {

    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "debtor", target = "debtor")
    @Mapping(source = "creditor", target = "creditor")
    OweEntity toOwe(Owe owe);

    @Mapping(source = "debtor", target = "debtor.username")
    @Mapping(source = "creditor", target = "creditor.username")
    Owe oweSummaryProjectionToOwe(OweSummaryProjection oweSummaryProjection);

    List<Owe> oweSummaryProjectionToOwe(List<OweSummaryProjection> oweSummaryProjection);

}
