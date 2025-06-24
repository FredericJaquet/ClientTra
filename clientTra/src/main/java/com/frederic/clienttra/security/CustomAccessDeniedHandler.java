package com.frederic.clienttra.security;

import com.frederic.clienttra.utils.MessageResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    private final MessageResolver messageResolver;

    public CustomAccessDeniedHandler(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

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
        String json = String.format("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"%s\",\"path\":\"%s\"}",
                message, request.getRequestURI());
        response.getWriter().write(json);
    }
}

