package com.frederic.clienttra.config;

import com.frederic.clienttra.security.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

@Configuration
public class LocaleConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new LocaleResolver() {
            private final SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();

            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails) {
                    String preferredLang = ((CustomUserDetails) auth.getPrincipal()).getPreferredLanguage();
                    if (preferredLang != null && !preferredLang.isEmpty()) {
                        return Locale.forLanguageTag(preferredLang);
                    }
                }

                // fallback: usa el locale de sesión o el locale por defecto del navegador
                Locale sessionLocale = sessionLocaleResolver.resolveLocale(request);
                return (sessionLocale != null) ? sessionLocale : Locale.getDefault();
            }

            @Override
            public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
                sessionLocaleResolver.setLocale(request, response, locale);
            }
        };
    }
}
