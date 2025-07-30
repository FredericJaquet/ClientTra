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

/**
 * Configuration class responsible for resolving the user's locale based on their
 * preferred language stored in the authenticated user details.
 */
@Configuration
public class LocaleConfig {

    /**
     * Defines a custom {@link LocaleResolver} bean that:
     * <ul>
     *     <li>Uses the preferred language set in the {@link CustomUserDetails} if available.</li>
     *     <li>Falls back to the session locale or system default locale otherwise.</li>
     * </ul>
     *
     * @return a custom {@link LocaleResolver} implementation
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new LocaleResolver() {

            // Delegate to SessionLocaleResolver as a fallback mechanism
            private final SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();

            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                // Check if user is authenticated and has a preferred language set
                if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails) {
                    String preferredLang = ((CustomUserDetails) auth.getPrincipal()).getPreferredLanguage();
                    if (preferredLang != null && !preferredLang.isEmpty()) {
                        return Locale.forLanguageTag(preferredLang);
                    }
                }

                // Fallback: use the session's locale or the browser's default locale
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
