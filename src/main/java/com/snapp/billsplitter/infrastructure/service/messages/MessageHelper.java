package com.snapp.billsplitter.infrastructure.service.messages;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageHelper {
    private final MessageSource messageSource;

    public String getMessage(String code) {
        return getMessage(code,null);
    }

    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code,args, Locale.getDefault());
    }
}