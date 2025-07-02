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

@RestController
@RequestMapping("/api/companies/{idCompany}/contacts")
@RequiredArgsConstructor
public class ContactPersonController {

    private final ContactPersonService contactPersonService;

    @GetMapping
    public List<ContactPersonDTO> getAllContactPersons(@PathVariable Integer idCompany) {
        return contactPersonService.getAllContactPersons(idCompany);
    }

    @GetMapping("/{idContactPerson}")
    public ContactPersonDTO getContactPersonById(@PathVariable Integer idCompany, @PathVariable Integer idContactPerson) {
        return contactPersonService.getContactPerson(idCompany, idContactPerson);
    }

    @DeleteMapping("/{idContactPerson}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteContactPerson(@PathVariable Integer idCompany, @PathVariable Integer idContactPerson) {
        contactPersonService.deleteContactPerson(idCompany, idContactPerson);
        return ResponseEntity.ok(new GenericResponseDTO("contact.deleted.success"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createContactPerson(@PathVariable Integer idCompany,@Valid @RequestBody CreateContactPersonRequestDTO dto) {

        contactPersonService.createContactPerson(idCompany, dto);
        return ResponseEntity.ok(new GenericResponseDTO("contact.created.success"));
    }

    @PatchMapping("/{idContactPerson}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ContactPersonDTO updateContactPerson(@PathVariable Integer idCompany,@PathVariable Integer idContactPerson, @RequestBody UpdateContactPersonRequestDTO dto) {

        return contactPersonService.updateContactPerson(idCompany, idContactPerson, dto);
    }
}
