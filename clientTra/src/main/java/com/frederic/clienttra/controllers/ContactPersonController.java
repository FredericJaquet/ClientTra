package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateContactPersonRequestDTO;
import com.frederic.clienttra.dto.read.ContactPersonDTO;
import com.frederic.clienttra.dto.update.UpdateContactPersonRequestDTO;
import com.frederic.clienttra.services.ContactPersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing contact persons related to a specific company.
 * <p>
 * Provides endpoints to list, retrieve, create, update, and delete contact persons.
 * Only users with ADMIN or ACCOUNTING roles can perform modifications.
 */
@RestController
@RequestMapping("/api/companies/{idCompany}/contacts")
@RequiredArgsConstructor
public class ContactPersonController {

    private final ContactPersonService contactPersonService;

    /**
     * Retrieves all contact persons associated with a given company.
     *
     * @param idCompany the ID of the company
     * @return a list of {@link ContactPersonDTO}
     */
    @GetMapping
    public ResponseEntity<List<ContactPersonDTO>> getAllContactPersons(@PathVariable Integer idCompany) {
        return ResponseEntity.ok(contactPersonService.getAllContactPersons(idCompany));
    }

    /**
     * Retrieves a specific contact person by ID, scoped to a given company.
     *
     * @param idCompany        the ID of the company
     * @param idContactPerson  the ID of the contact person
     * @return the corresponding {@link ContactPersonDTO}
     */
    @GetMapping("/{idContactPerson}")
    public ResponseEntity<ContactPersonDTO> getContactPersonById(@PathVariable Integer idCompany, @PathVariable Integer idContactPerson) {
        return ResponseEntity.ok(contactPersonService.getContactPerson(idCompany, idContactPerson));
    }

    /**
     * Deletes a contact person by ID.
     * Only users with ADMIN or ACCOUNTING roles are allowed.
     *
     * @param idCompany        the ID of the company
     * @param idContactPerson  the ID of the contact person to delete
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping("/{idContactPerson}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteContactPerson(@PathVariable Integer idCompany, @PathVariable Integer idContactPerson) {
        contactPersonService.deleteContactPerson(idCompany, idContactPerson);
        return ResponseEntity.ok(new GenericResponseDTO("contact.deleted.success"));
    }

    /**
     * Creates a new contact person for the specified company.
     * Only users with ADMIN or ACCOUNTING roles are allowed.
     *
     * @param idCompany the ID of the company
     * @param dto       the request payload containing the contact person's data
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createContactPerson(@PathVariable Integer idCompany, @Valid @RequestBody CreateContactPersonRequestDTO dto) {
        contactPersonService.createContactPerson(idCompany, dto);
        return ResponseEntity.ok(new GenericResponseDTO("contact.created.success"));
    }

    /**
     * Updates an existing contact person by ID.
     * Only users with ADMIN or ACCOUNTING roles are allowed.
     *
     * @param idCompany        the ID of the company
     * @param idContactPerson  the ID of the contact person to update
     * @param dto              the request payload containing updated data
     * @return the updated {@link ContactPersonDTO}
     */
    @PatchMapping("/{idContactPerson}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<ContactPersonDTO> updateContactPerson(@PathVariable Integer idCompany, @PathVariable Integer idContactPerson, @RequestBody UpdateContactPersonRequestDTO dto) {
        return ResponseEntity.ok(contactPersonService.updateContactPerson(idCompany, idContactPerson, dto));
    }
}
