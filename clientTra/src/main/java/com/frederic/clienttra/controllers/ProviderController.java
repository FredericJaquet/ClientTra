package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateProviderRequestDTO;
import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.dto.read.ProviderDetailsDTO;
import com.frederic.clienttra.dto.read.ProviderForListDTO;
import com.frederic.clienttra.dto.update.UpdateProviderRequestDTO;
import com.frederic.clienttra.services.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @GetMapping
    public ResponseEntity<List<ProviderForListDTO>> getAllProviders(){
        return ResponseEntity.ok(providerService.getAllProviders());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createProvider(@Valid @RequestBody CreateProviderRequestDTO dto){
        int newId = providerService.createProvider(dto);

        return ResponseEntity
                .created(URI.create("/api/customers/"+newId))
                .body(new GenericResponseDTO("provider.created.success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderDetailsDTO> getProviderById(@PathVariable int id){
        return ResponseEntity.ok(providerService.getProviderById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<ProviderDetailsDTO> updateProvider(@PathVariable int id, @RequestBody UpdateProviderRequestDTO dto){
        providerService.updateProvider(id, dto);
        return ResponseEntity.ok(providerService.getProviderById(id));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteProvider(@PathVariable int id){
        providerService.disableProvider(id);
        return ResponseEntity.ok(new GenericResponseDTO(("provider.deleted.success")));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProviderForListDTO>> searchProvider(@RequestParam String input){
        return ResponseEntity.ok(providerService.searchByNameOrVat(input));
    }

    @GetMapping("minimal-list")
    public ResponseEntity<List<BaseCompanyMinimalDTO>> getMinimalProviders(){
        return ResponseEntity.ok(providerService.getMinimalProviderList());
    }
}
