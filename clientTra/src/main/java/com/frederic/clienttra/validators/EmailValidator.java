package com.frederic.clienttra.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Utility class for validating email addresses.
 */
@Component
@RequiredArgsConstructor
public class EmailValidator {

    /**
     * Checks if the provided email string is a valid email format.
     *
     * @param email the email string to validate
     * @return true if the email is non-null, non-blank, and matches a basic email pattern; false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) return false;
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
