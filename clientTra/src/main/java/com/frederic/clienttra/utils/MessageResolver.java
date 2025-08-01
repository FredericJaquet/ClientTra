package com.frederic.clienttra.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Component to resolve internationalized messages (i18n) using the configured
 * Spring MessageSource and the current locale from the context.
 */
@Component
public class MessageResolver {

    private final MessageSource messageSource;

    /**
     * Constructor that injects the MessageSource.
     *
     * @param messageSource the MessageSource to access i18n messages.
     */
    public MessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Retrieves the internationalized message for the given code,
     * returning the code itself as a fallback if no message is found.
     *
     * @param code the message code to look up in message files.
     * @return the translated message or the code if no translation exists.
     */
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, code, LocaleContextHolder.getLocale());
    }

    /**
     * Retrieves the internationalized message for the given code,
     * returning a fallback message if the code is not found.
     *
     * @param code            the message code to look up.
     * @param fallbackMessage the message to return if no translation is found.
     * @return the translated message or the fallback message.
     */
    public String getMessage(String code, String fallbackMessage) {
        return messageSource.getMessage(code, null, fallbackMessage, LocaleContextHolder.getLocale());
    }
}
