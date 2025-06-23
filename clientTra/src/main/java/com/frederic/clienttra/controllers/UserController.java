package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.UserForAdminDTO;
import com.frederic.clienttra.dto.UserSelfDTO;
import com.frederic.clienttra.exceptions.UserErrorResponse;
import com.frederic.clienttra.exceptions.UserErrorResponseException;
import com.frederic.clienttra.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
                .orElseThrow(() -> new UserErrorResponseException("Empleado con id=" + id + " no encontrado."));
    }

    @GetMapping("/me")
    public UserSelfDTO getSelfUser() {
        return userService.getCurrentUserDetails();
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> exceptionHandler(UserErrorResponseException e){

        UserErrorResponse error=new UserErrorResponse();

        error.setState(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }
}
