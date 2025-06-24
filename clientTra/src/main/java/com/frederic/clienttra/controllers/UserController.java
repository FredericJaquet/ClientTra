package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.*;
import com.frederic.clienttra.exceptions.ErrorResponse;
import com.frederic.clienttra.exceptions.UserErrorResponse;
import com.frederic.clienttra.exceptions.UserErrorResponseException;
import com.frederic.clienttra.exceptions.UserNotFoundException;
import com.frederic.clienttra.services.UserService;
import com.frederic.clienttra.utils.MessageResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final MessageResolver messageResolver;

    public UserController(UserService userService, MessageResolver messageResolver) {
        this.userService = userService;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserForAdminDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserForAdminDTO> getUserById(@PathVariable int id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/me")
    public UserSelfDTO getSelfUser() {
        return userService.getCurrentUserDetails();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> createUser(
            @Valid @RequestBody CreateUserRequestDTO dto) {
        userService.createUser(dto);
        String message = messageResolver.getMessage("user.created.success", "Usuario creado con éxito");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseDTO(message));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserSelfDTO> updateSelf(@RequestBody @Valid UpdateSelfRequestDTO dto) {
        userService.updateCurrentUser(dto);
        return ResponseEntity.ok(userService.getCurrentUserDetails());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        String msg = messageResolver.getMessage("user.deleted.success", "Usuario eliminado correctamente");
        return ResponseEntity.ok(new GenericResponseDTO(msg));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "Usuario no encontrado", request.getRequestURI());
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
