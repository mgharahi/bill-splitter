package com.snapp.billsplitter.infrastructure.mapper.auth;


import com.snapp.billsplitter.infrastructure.controller.dto.AuthRequest;
import com.snapp.billsplitter.infrastructure.controller.dto.TokenPackage;
import com.snapp.billsplitter.infrastructure.service.auth.dto.Credential;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "refreshToken", source = "refreshToken")
    Credential toCredential(AuthRequest authRequest);

    TokenPackage toTokenPackage(com.snapp.billsplitter.infrastructure.service.auth.dto.TokenPackage tokens);

    com.snapp.billsplitter.infrastructure.service.auth.dto.TokenPackage toTokenPackage(TokenPackage tokens);
}