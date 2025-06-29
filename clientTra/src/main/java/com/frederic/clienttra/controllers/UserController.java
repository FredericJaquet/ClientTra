package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.*;
import com.frederic.clienttra.dto.create.CreateUserRequestDTO;
import com.frederic.clienttra.dto.read.UserForAdminDTO;
import com.frederic.clienttra.dto.read.UserSelfDTO;
import com.frederic.clienttra.dto.update.UpdatePasswordRequestDTO;
import com.frederic.clienttra.dto.update.UpdateSelfRequestDTO;
import com.frederic.clienttra.exceptions.UserNotFoundException;
import com.frederic.clienttra.services.UserService;
import com.frederic.clienttra.utils.MessageResolver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageResolver messageResolver;

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
        String msg = messageResolver.getMessage("user.created.success", "Usuario creado con éxito");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseDTO(msg));
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

    @PatchMapping("/reactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> reactivateUser(@PathVariable int id) {
        userService.reactivateUserById(id);
        String msg = messageResolver.getMessage("user.reactivate.success", "EL usuario ha sido restablecido correctamente.");
        return ResponseEntity.ok(new GenericResponseDTO(msg));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<GenericResponseDTO> changePassword(
            @Valid @RequestBody UpdatePasswordRequestDTO dto) {
        userService.changePassword(dto);
        String msg = messageResolver.getMessage("user.password.changed", "Contraseña modificada correctamente");
        return ResponseEntity.ok(new GenericResponseDTO(msg));
    }

}
