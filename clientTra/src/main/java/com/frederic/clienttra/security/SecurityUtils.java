package com.frederic.clienttra.security;

import com.frederic.clienttra.exceptions.UserNotAuthenticatedException;
import com.frederic.clienttra.utils.MessageResolver;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for security-related helper methods.
 * <p>
 * Provides static methods to retrieve information about the currently authenticated user
 * from the Spring Security context.
 */
public class SecurityUtils {

    @Setter
    private static MessageResolver messageResolver;

    /**
     * Retrieves the company ID of the currently authenticated user.
     *
     * @return the company ID of the authenticated user
     * @throws UserNotAuthenticatedException if no user is authenticated or the authentication principal
     *                                       is not an instance of {@link CustomUserDetails}
     */
    public static int getCurrentUserCompanyId() {
        CustomUserDetails userDetails = getAuthenticatedUser();
        return userDetails.getIdCompany();
    }

    /**
     * Retrieves the user ID of the currently authenticated user.
     *
     * @return the user ID of the authenticated user
     * @throws UserNotAuthenticatedException if no user is authenticated or the authentication principal
     *                                       is not an instance of {@link CustomUserDetails}
     */
    public static int getCurrentUserId() {
        CustomUserDetails userDetails = getAuthenticatedUser();
        return userDetails.getIdUser();
    }

    /**
     * Gets the {@link CustomUserDetails} of the currently authenticated user from the security context.
     *
     * @return the authenticated user's details
     * @throws UserNotAuthenticatedException if no user is authenticated or the authentication principal
     *                                       is not an instance of {@link CustomUserDetails}
     */
    private static CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UserNotAuthenticatedException();
        }

        return (CustomUserDetails) authentication.getPrincipal();
    }
}
