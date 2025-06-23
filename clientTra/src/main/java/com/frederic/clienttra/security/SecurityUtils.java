package com.frederic.clienttra.security;

import com.frederic.clienttra.exceptions.UserNotAuthenticatedException;
import com.frederic.clienttra.utils.MessageResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SecurityUtils {

    public static int getCurrentUserCompanyId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UserNotAuthenticatedException("No hay usuario autenticado");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getIdCompany();
    }

    public static int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UserNotAuthenticatedException("No hay usuario autenticado");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getIdUser();
    }
}

