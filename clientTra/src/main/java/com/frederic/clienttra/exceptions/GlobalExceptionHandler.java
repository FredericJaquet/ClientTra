package com.frederic.clienttra.exceptions;

import com.frederic.clienttra.utils.MessageResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Global exception handler to intercept exceptions thrown by controllers
 * and return standardized error responses with appropriate HTTP status codes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageResolver messageResolver;

    public GlobalExceptionHandler(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    // --- Authentication and authorization ---

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotAuthenticated(UserNotAuthenticatedException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                "Could not determine the authenticated user's company.",
                request.getRequestURI());
    }

    @ExceptionHandler({
            org.springframework.security.access.AccessDeniedException.class,
            org.springframework.security.authorization.AuthorizationDeniedException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ErrorResponse> handleAccessDeniedExceptions(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                "Access denied. You do not have permission to perform this action.",
                request.getRequestURI());
    }

    // --- Resources not found (404) ---

    @ExceptionHandler({
            UserNotFoundException.class,
            CompanyNotFoundForUserException.class,
            CompanyNotFoundException.class,
            CustomerNotFoundException.class,
            ContactPersonNotFoundException.class,
            AddressNotFoundException.class,
            BankAccountNotFoundException.class,
            PhoneNotFoundException.class,
            RoleNotFoundException.class,
            SchemeNotFoundException.class,
            ProviderNotFoundException.class,
            OrderNotFoundException.class,
            ChangeRateNotFoundException.class,
            DocumentNotFoundException.class,
            LastNumberNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "The requested resource does not exist or does not belong to your company.",
                request.getRequestURI());
    }

    // --- Custom manual validation errors ---
    @ExceptionHandler(ManualValidationException.class)
    public ResponseEntity<ErrorResponse> handleManualValidationException(ManualValidationException ex, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        ex.getFieldErrors().forEach((field, msgKey) -> {
            String message = messageResolver.getMessage(msgKey);
            sb.append(field).append(": ").append(message);
        });

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                sb.toString().trim(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // --- Validation errors triggered by @Valid annotations ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String message = messageResolver.getMessage(fieldError.getDefaultMessage());
            sb.append(fieldError.getField()).append(": ").append(message);
        }

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                sb.toString().trim(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // --- Simple validation and conflict errors ---
    @ExceptionHandler({
            InvalidEmailException.class,
            InvalidIbanException.class,
            InvalidLegalNameException.class,
            InvalidPasswordException.class,
            InvalidVatNumberException.class,
            InvalidSchemeNameException.class,
            InvalidSchemePriceException.class,
            CompanyAlreadyExistsException.class,
            LogoNotLoadedException.class,
            LastAddressException.class,
            CantDeleteSelfException.class,
            InvalidOrderDescriptionException.class,
            InvalidOrderDateException.class,
            InvalidOrderPriceException.class,
            CantCreateDocumentWithoutOrdersException.class,
            CantModifyPaidInvoiceException.class,
            CantIncludeOrderAlreadyBilledException.class,
            InvalidVatRateException.class,
            InvalidWithholdingException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidInput(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                "Invalid data or operation conflict.",
                request.getRequestURI());
    }

    // --- Generic catch-all exception handler ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                "An unexpected error occurred.",
                request.getRequestURI());
    }

    // --- Utility method to build ErrorResponse and ResponseEntity ---

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String code, String fallbackMessage, String path) {
        String localizedMsg = messageResolver.getMessage(code);
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                (localizedMsg != null && !localizedMsg.isBlank()) ? localizedMsg : fallbackMessage,
                path
        );
        return new ResponseEntity<>(error, status);
    }
}
