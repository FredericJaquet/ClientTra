package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateUserRequestDTO;
import com.frederic.clienttra.dto.create.RegistrationRequestDTO;
import com.frederic.clienttra.services.DemoDataService;
import com.frederic.clienttra.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final DemoDataService demoDataService;

    @PostMapping("/actual-data")
    public ResponseEntity<GenericResponseDTO> registerActualCompany(@Valid @RequestBody RegistrationRequestDTO dto) {
        registrationService.register(dto);
        return ResponseEntity.ok(new GenericResponseDTO("registration.created.success"));
    }

    @PostMapping("/demo-data")
    public ResponseEntity<GenericResponseDTO> registerDemoCompany(@Valid @RequestBody CreateUserRequestDTO dto){
        demoDataService.loadData(dto);
        return ResponseEntity.ok(new GenericResponseDTO("registration.created_with_demo.success"));
    }

}

