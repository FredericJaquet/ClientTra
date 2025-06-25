package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.*;
import com.frederic.clienttra.exceptions.CompanyNotFoundForUserException;
import com.frederic.clienttra.services.CompanyServiceImpl;
import com.frederic.clienttra.utils.MessageResolver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final CompanyServiceImpl companyService;
    private final MessageResolver messageResolver;

    @GetMapping
    public ResponseEntity<CompanyOwnerDTO> getCompany() {
       return companyService.getCompanyOwner()
                .map(ResponseEntity::ok)
                .orElseThrow(CompanyNotFoundForUserException::new);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<CompanyOwnerDTO>> updateOwner(@RequestBody @Valid UpdateCompanyOwnerDTO dto){
        companyService.updateCompanyOwner(dto);
        return ResponseEntity.ok(companyService.getCompanyOwner());
    }

    @PostMapping("/logo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> uploadCompanyLogo(@RequestParam("logo") MultipartFile file) {
        companyService.uploadCompanyLogo(file);
        String msg = messageResolver.getMessage("logo.upload.success", "Logo actualizado correctamente");
        return ResponseEntity.ok(new GenericResponseDTO(msg));
    }


}
