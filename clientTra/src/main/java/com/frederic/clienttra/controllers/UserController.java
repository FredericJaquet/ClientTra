package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.*;
import com.frederic.clienttra.dto.create.CreateUserRequestDTO;
import com.frederic.clienttra.dto.read.UserForAdminDTO;
import com.frederic.clienttra.dto.read.UserSelfDTO;
import com.frederic.clienttra.dto.update.UpdatePasswordRequestDTO;
import com.frederic.clienttra.dto.update.UpdateSelfRequestDTO;
import com.frederic.clienttra.dto.update.UpdateUserForAdminDTO;
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

/**
 * REST Controller for managing users.
 * <p>
 * Provides endpoints for CRUD operations, self-profile management,
 * password changes, and user activation/deactivation.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageResolver messageResolver;

    /**
     * Retrieves all users.
     * Requires ADMIN role.
     *
     * @return list of users for admin view
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserForAdminDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieves a user by their ID.
     * Requires ADMIN role.
     *
     * @param id the user ID
     * @return user details for admin
     * @throws UserNotFoundException if user does not exist
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserForAdminDTO> getUserById(@PathVariable int id) {
        UserForAdminDTO user = userService.getUserById(id)
                .orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves the currently authenticated user's own profile details.
     *
     * @return current user profile data
     */
    @GetMapping("/me")
    public ResponseEntity<UserSelfDTO> getSelfUser() {
        return ResponseEntity.ok(userService.getCurrentUserDetails());
    }

    /**
     * Creates a new user.
     * Requires ADMIN role.
     *
     * @param dto data for creating a user
     * @return success message with HTTP 201 Created status
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserForAdminDTO> createUser(
            @Valid @RequestBody CreateUserRequestDTO dto) {
        UserForAdminDTO createdUser = userService.createUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Updates the currently authenticated user's own profile.
     *
     * @param dto data to update the user profile
     * @return updated user profile details
     */
    @PatchMapping("/me")
    public ResponseEntity<UserSelfDTO> updateSelf(@RequestBody @Valid UpdateSelfRequestDTO dto) {
        userService.updateCurrentUser(dto);
        return ResponseEntity.ok(userService.getCurrentUserDetails());
    }

    /**
     * Soft deletes (deactivates) a user by ID.
     * Requires ADMIN role.
     *
     * @param id the user ID to delete
     * @return success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        String msg = messageResolver.getMessage("user.deleted.success", "User successfully deleted");
        return ResponseEntity.ok(new GenericResponseDTO(msg));
    }

    /**
     * Reactivates a previously deactivated user by ID.
     * Requires ADMIN role.
     *
     * @param id the user ID to reactivate
     * @return success message
     */
    @PatchMapping("/reactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> reactivateUser(@PathVariable int id) {
        userService.reactivateUserById(id);
        String msg = messageResolver.getMessage("user.reactivate.success", "User successfully reactivated");
        return ResponseEntity.ok(new GenericResponseDTO(msg));
    }

    /**
     * Updates a user.
     * Requires ADMIN role.
     *
     * @return success message
     */
    @PatchMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserForAdminDTO> updateUser(@RequestBody UpdateUserForAdminDTO dto) {

        return ResponseEntity.ok(userService.updateUser(dto));
    }

    /**
     * Changes the password of the currently authenticated user.
     *
     * @param dto data containing old and new password
     * @return success message
     */
    @PatchMapping("/me/password")
    public ResponseEntity<GenericResponseDTO> changePassword(@Valid @RequestBody UpdatePasswordRequestDTO dto) {
        userService.changePassword(dto);
        String msg = messageResolver.getMessage("user.password.changed", "Password successfully changed");
        return ResponseEntity.ok(new GenericResponseDTO(msg));
    }

}
