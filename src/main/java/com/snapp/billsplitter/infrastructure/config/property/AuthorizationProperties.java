package com.snapp.billsplitter.infrastructure.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bill.splitter.authorization")
public record AuthorizationProperties(Header header, Token token) {

    public record Header(String name) {
    }

    public record Token(String prefix) {
    }
}