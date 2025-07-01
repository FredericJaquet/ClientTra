package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreatePhoneRequestDTO;
import com.frederic.clienttra.dto.read.PhoneDTO;
import com.frederic.clienttra.dto.update.UpdatePhoneRequestDTO;
import com.frederic.clienttra.services.PhoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies/{idCompany}/phones")
@RequiredArgsConstructor
public class PhoneController {

    private final PhoneService phoneService;

    @GetMapping
    public List<PhoneDTO> getAllPhones(@PathVariable Integer idCompany) {
        return phoneService.getAllPhones(idCompany);
    }

    @GetMapping("/{idPhone}")
    public PhoneDTO getPhoneById(@PathVariable Integer idCompany, @PathVariable Integer idPhone) {
        return phoneService.getPhone(idCompany, idPhone);
    }

    @DeleteMapping("/{idPhone}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteAddress(@PathVariable Integer idCompany, @PathVariable Integer idPhone) {
        phoneService.deletePhone(idCompany, idPhone);
        return ResponseEntity.ok(new GenericResponseDTO("phone.deleted.success"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createPhone(@PathVariable Integer idCompany,@Valid @RequestBody CreatePhoneRequestDTO dto) {

        phoneService.createPhone(idCompany, dto);
        return ResponseEntity.ok(new GenericResponseDTO("phone.created.success"));
    }

    @PatchMapping("/{idPhone}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public PhoneDTO updatePhone(@PathVariable Integer idCompany,@PathVariable Integer idPhone, @RequestBody UpdatePhoneRequestDTO dto) {

        return phoneService.updatePhone(idCompany, idPhone, dto);
    }
}
