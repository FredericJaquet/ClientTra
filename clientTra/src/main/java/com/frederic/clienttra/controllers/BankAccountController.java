package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateBankAccountRequestDTO;
import com.frederic.clienttra.dto.read.BankAccountDTO;
import com.frederic.clienttra.dto.update.UpdateBankAccountRequestDTO;
import com.frederic.clienttra.services.BankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing bank accounts associated with a specific company.
 * <p>
 * Provides endpoints to list, retrieve, create, update, and delete bank accounts.
 * Access to modification operations is restricted to users with roles ADMIN or ACCOUNTING.
 */
@RestController
@RequestMapping("/api/companies/{idCompany}/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    /**
     * Retrieves all bank accounts belonging to the specified company.
     *
     * @param idCompany the ID of the company
     * @return a list of {@link BankAccountDTO}
     */
    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts(@PathVariable Integer idCompany) {
        return ResponseEntity.ok(bankAccountService.getAllBankAccounts(idCompany));
    }

    /**
     * Retrieves a single bank account by its ID, scoped to the specified company.
     *
     * @param idCompany      the ID of the company
     * @param idBankAccount  the ID of the bank account
     * @return the {@link BankAccountDTO} for the requested bank account
     */
    @GetMapping("/{idBankAccount}")
    public ResponseEntity<BankAccountDTO> getBankAccountById(@PathVariable Integer idCompany, @PathVariable Integer idBankAccount) {
        return ResponseEntity.ok(bankAccountService.getBankAccount(idCompany, idBankAccount));
    }

    /**
     * Deletes a bank account by its ID. Only users with ADMIN or ACCOUNTING roles are authorized.
     *
     * @param idCompany      the ID of the company
     * @param idBankAccount  the ID of the bank account to delete
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping("/{idBankAccount}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteBankAccount(@PathVariable Integer idCompany, @PathVariable Integer idBankAccount) {
        bankAccountService.deleteBankAccount(idCompany, idBankAccount);
        return ResponseEntity.ok(new GenericResponseDTO("account.deleted.success"));
    }

    /**
     * Creates a new bank account for the specified company.
     * Only users with ADMIN or ACCOUNTING roles are authorized.
     *
     * @param idCompany the ID of the company
     * @param dto       the bank account creation request
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createBankAccount(@PathVariable Integer idCompany, @Valid @RequestBody CreateBankAccountRequestDTO dto) {
        bankAccountService.createBankAccount(idCompany, dto);
        return ResponseEntity.ok(new GenericResponseDTO("account.created.success"));
    }

    /**
     * Updates an existing bank account by its ID.
     * Only users with ADMIN or ACCOUNTING roles are authorized.
     *
     * @param idCompany      the ID of the company
     * @param idBankAccount  the ID of the bank account to update
     * @param dto            the bank account update request
     * @return the updated {@link BankAccountDTO}
     */
    @PatchMapping("/{idBankAccount}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<BankAccountDTO> updateBankAccount(@PathVariable Integer idCompany, @PathVariable Integer idBankAccount, @RequestBody UpdateBankAccountRequestDTO dto) {
        return ResponseEntity.ok(bankAccountService.updateBankAccount(idCompany, idBankAccount, dto));
    }

}