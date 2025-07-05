package com.frederic.clienttra.exceptions;

import com.frederic.clienttra.utils.MessageResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageResolver messageResolver;

    public GlobalExceptionHandler(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    // --- Autenticación y autorización ---
    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotAuthenticated(UserNotAuthenticatedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), "No se pudo determinar la compañía del usuario autenticado.", request.getRequestURI());
    }

    @ExceptionHandler({
            org.springframework.security.access.AccessDeniedException.class,
            org.springframework.security.authorization.AuthorizationDeniedException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ErrorResponse> handleAccessDeniedExceptions(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), "Acceso denegado. No tienes permisos para realizar esta acción.", request.getRequestURI());
    }

    // --- Recursos no encontrados (404) ---
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
            ProviderNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "El recurso solicitado no existe o no pertenece a tu compañía.", request.getRequestURI());
    }

    // --- Validaciones manuales personalizadas ---

    @ExceptionHandler(ManualValidationException.class)
    public ResponseEntity<ErrorResponse> handleManualValidationException(ManualValidationException ex, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        ex.getFieldErrors().forEach((field, msgKey) -> {
            String message = messageResolver.getMessage(msgKey);
            sb.append(field).append(": ").append(message).append("; ");
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

    // --- Validaciones con anotaciones (e.g. @Valid) ---

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String message = messageResolver.getMessage(fieldError.getDefaultMessage());
            sb.append(fieldError.getField()).append(": ").append(message).append("; ");
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

    // --- Validaciones simples (campos incorrectos, ya existe, etc.) ---

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
            CantDeleteSelfException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidInput(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), "Datos inválidos o conflicto con la operación.", request.getRequestURI());
    }

    // --- Genérico (último recurso) ---

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "Ha ocurrido un error inesperado.", request.getRequestURI());
    }

    // --- Utilidad centralizada ---

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
