package com.frederic.clienttra.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * DTO for updating base company information.
 * Contains fields for VAT number, commercial name, legal name, email, and website.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UpdateBaseCompanyRequestDTO {
    protected String vatNumber;
    protected String comName;
    protected String legalName;
    protected String email;
    protected String web;
}
