package com.frederic.clienttra.security;

import com.frederic.clienttra.utils.MessageResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Custom implementation of Spring Security's {@link AccessDeniedHandler}.
 * <p>
 * Handles cases where a user tries to access a resource without the necessary permissions.
 * Returns a JSON response with HTTP status 403 (Forbidden) and a localized error message.
 * Also logs the denied access event.
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    private final MessageResolver messageResolver;

    /**
     * Handles access denied failure.
     * Logs the incident, sets the HTTP response status to 403,
     * and returns a JSON body with a localized error message.
     *
     * @param request  the HttpServletRequest during which the access was denied
     * @param response the HttpServletResponse to send the error details
     * @param accessDeniedException the exception thrown due to access denial
     * @throws IOException      if an input or output error occurs while handling the response
     * @throws ServletException if the request could not be handled
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.warn("Acceso denegado para la URL: " + request.getRequestURI() + ", mensaje: " + accessDeniedException.getMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        String message;
        try {
            message = messageResolver.getMessage("error.access_denied");
        } catch (Exception e) {
            message = "Acceso denegado (mensaje no disponible)";
        }

        String json = String.format(
                "{\"status\":403,\"error\":\"Forbidden\",\"message\":\"%s\",\"path\":\"%s\"}",
                message, request.getRequestURI());

        response.getWriter().write(json);
    }
}
