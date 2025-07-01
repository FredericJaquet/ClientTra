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

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotAuthenticated(UserNotAuthenticatedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), "No se pudo determinar la compañía del usuario autenticado.", request.getRequestURI());
    }

    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDenied(org.springframework.security.authorization.AuthorizationDeniedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), "Acceso denegado. No tienes permisos para realizar esta acción.", request.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), "Acceso denegado. No tienes permisos para realizar esta acción.", request.getRequestURI());
    }

    @ExceptionHandler(CantDeleteSelfException.class)
    public ResponseEntity<ErrorResponse> handleCantDeleteSelf(CantDeleteSelfException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), "No hemos podido encontrar el usuario.", request.getRequestURI());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "No hemos podido encontrar el usuario.", request.getRequestURI());
    }

    @ExceptionHandler(CompanyNotFoundForUserException.class)
    public ResponseEntity<ErrorResponse> handleCompanyNotFoundForUser(CompanyNotFoundForUserException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "No se encontró la compañía del usuario autenticado", request.getRequestURI());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(RoleNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "El recurso solicitado no existe.", request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "Ha ocurrido un error inesperado.", request.getRequestURI());
    }

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

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String code, String fallbackMessage, String path) {
        String localizedMsg = messageResolver.getMessage(code);
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                localizedMsg != null ? localizedMsg : fallbackMessage,
                path
        );
        return new ResponseEntity<>(error, status);
    }
}
