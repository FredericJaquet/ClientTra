package com.frederic.clienttra.security;

import com.frederic.clienttra.exceptions.UserNotAuthenticatedException;
import com.frederic.clienttra.utils.MessageResolver;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    @Setter
    private static MessageResolver messageResolver;

    public static int getCurrentUserCompanyId() {
        CustomUserDetails userDetails = getAuthenticatedUser();
        return userDetails.getIdCompany();
    }

    public static int getCurrentUserId() {
        CustomUserDetails userDetails = getAuthenticatedUser();
        return userDetails.getIdUser();
    }

    private static CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
           throw new UserNotAuthenticatedException();
        }

        return (CustomUserDetails) authentication.getPrincipal();
    }
}

