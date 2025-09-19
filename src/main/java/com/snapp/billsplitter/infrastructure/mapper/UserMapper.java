package com.snapp.billsplitter.infrastructure.mapper;

import com.snapp.billsplitter.core.domain.Owe;
import com.snapp.billsplitter.core.domain.User;
import com.snapp.billsplitter.infrastructure.spring.entity.OweEntity;
import com.snapp.billsplitter.infrastructure.spring.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id", target = "id",qualifiedByName = "stringToLong")
    UserEntity toUserEntity(User owe);


    @Named("stringToLong")
    default Long stringToLong(String id) {
        if (id == null) return null;
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
