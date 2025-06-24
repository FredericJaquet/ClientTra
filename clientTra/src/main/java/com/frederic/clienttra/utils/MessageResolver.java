package com.frederic.clienttra.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageResolver {

    private final MessageSource messageSource;

    public MessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, code, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, String fallbackMessage) {
        return messageSource.getMessage(code, null, fallbackMessage, LocaleContextHolder.getLocale());
    }
}

