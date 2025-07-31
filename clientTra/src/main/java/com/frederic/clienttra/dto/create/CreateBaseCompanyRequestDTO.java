package com.frederic.clienttra.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Base DTO for creating a company.
 * <p>
 * Contains common fields required when creating a company, including
 * VAT number, commercial and legal names, email, web address, contact persons,
 * phones, addresses, and bank accounts.
 * <p>
 * Validation annotations enforce constraints such as required fields,
 * valid email format, and non-null addresses list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CreateBaseCompanyRequestDTO {
    @NotBlank(message = "{validation.company.vat_number_required}")
    protected String vatNumber;
    protected String comName;
    @NotBlank(message = "{validation.company.legal_name_required}")
    protected String legalName;
    @Email(message = "{validation.email.invalid}")
    @Size(max = 100, message = "{validation.email.too_long}")
    protected String email;
    protected String web;
    private List<CreateContactPersonRequestDTO> contactPersons;
    private List<CreatePhoneRequestDTO> phones;
    @NotNull(message = "{validation.company.address_required}")
    private List<CreateAddressRequestDTO> addresses;
    private List<CreateBankAccountRequestDTO> bankAccounts;
}
