package com.frederic.clienttra.utils;

import com.frederic.clienttra.security.CustomUserDetails;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageResolver {

    private final MessageSource messageSource;

    public MessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        Locale locale = getUserLocale();
        return messageSource.getMessage(code, null, locale);
    }

    public String getMessage(String code, Object[] args) {
        Locale locale = getUserLocale();
        return messageSource.getMessage(code, args, locale);
    }

    private Locale getUserLocale() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails customUser) {
            String lang = customUser.getPreferredLanguage();
            return Locale.forLanguageTag(lang);
        }
        return Locale.getDefault();
    }
}
