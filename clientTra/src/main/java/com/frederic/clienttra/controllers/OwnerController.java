package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.*;
import com.frederic.clienttra.dto.read.CompanyOwnerDTO;
import com.frederic.clienttra.dto.update.UpdateCompanyOwnerRequestDTO;
import com.frederic.clienttra.exceptions.CompanyNotFoundForUserException;
import com.frederic.clienttra.services.CompanyServiceImpl;
import com.frederic.clienttra.utils.MessageResolver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Controller for managing the authenticated user's company (owner).
 * <p>
 * Provides endpoints to retrieve and update company details, as well as upload the company logo.
 * Access to modifications requires ADMIN role.
 */
@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final CompanyServiceImpl companyService;
    private final MessageResolver messageResolver;

    /**
     * Retrieves the company information associated with the current authenticated user.
     *
     * @return the {@link CompanyOwnerDTO} representing the company details
     * @throws CompanyNotFoundForUserException if the company is not found for the user
     */
    @GetMapping
    public ResponseEntity<CompanyOwnerDTO> getCompany() {
        CompanyOwnerDTO company = companyService.getCompanyOwnerDTO()
                .orElseThrow(CompanyNotFoundForUserException::new);
        return ResponseEntity.ok(company);
    }

    /**
     * Updates the company details.
     * Requires ADMIN role.
     *
     * @param dto the update data transfer object containing new company information
     * @return an optional {@link CompanyOwnerDTO} with the updated company information
     */
    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<CompanyOwnerDTO>> updateOwner(@RequestBody @Valid UpdateCompanyOwnerRequestDTO dto) {
        companyService.updateCompanyOwner(dto);
        return ResponseEntity.ok(companyService.getCompanyOwnerDTO());
    }

    /**
     * Uploads or updates the company's logo.
     * Requires ADMIN role.
     *
     * @param file the multipart file containing the logo image
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @PostMapping("/logo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> uploadCompanyLogo(@RequestParam("logo") MultipartFile file) {
        companyService.uploadCompanyLogo(file);
        String msg = messageResolver.getMessage("logo.upload.success", "Logo actualizado correctamente");
        return ResponseEntity.ok(new GenericResponseDTO(msg));
    }
}
