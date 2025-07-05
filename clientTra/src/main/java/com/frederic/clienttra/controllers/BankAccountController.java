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

@RestController
@RequestMapping("/api/companies/{idCompany}/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts(@PathVariable Integer idCompany) {
        return ResponseEntity.ok(bankAccountService.getAllBankAccounts(idCompany));
    }

    @GetMapping("/{idBankAccount}")
    public ResponseEntity<BankAccountDTO> getBankAccountById(@PathVariable Integer idCompany, @PathVariable Integer idBankAccount) {
        return ResponseEntity.ok(bankAccountService.getBankAccount(idCompany, idBankAccount));
    }

    @DeleteMapping("/{idBankAccount}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteBankAccount(@PathVariable Integer idCompany, @PathVariable Integer idBankAccount) {
        bankAccountService.deleteBankAccount(idCompany, idBankAccount);
        return ResponseEntity.ok(new GenericResponseDTO("account.deleted.success"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createBankAccount(@PathVariable Integer idCompany,@Valid @RequestBody CreateBankAccountRequestDTO dto) {

        bankAccountService.createBankAccount(idCompany, dto);
        return ResponseEntity.ok(new GenericResponseDTO("account.created.success"));
    }

    @PatchMapping("/{idBankAccount}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<BankAccountDTO> updateBankAccount(@PathVariable Integer idCompany,@PathVariable Integer idBankAccount, @RequestBody UpdateBankAccountRequestDTO dto) {

        return ResponseEntity.ok(bankAccountService.updateBankAccount(idCompany, idBankAccount, dto));
    }

}