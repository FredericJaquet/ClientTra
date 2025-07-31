package com.frederic.clienttra.dto.bases;

import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/**
 * Base interface for Document DTOs.
 * Defines common properties and methods for document-related data transfer objects.
 */
public interface BaseDocumentDTO {
    String getDocNumber();
    LocalDate getDocDate();
    DocumentType getDocType();
    DocumentStatus getStatus();
    String getLanguage();
    Double getVatRate();
    Double getWithholding();
    String getCurrency();
    String getNoteDelivery();
    String getNotePayment();
    String getNoteComment();
    LocalDate getDeadline();
    Integer getIdChangeRate();
    Integer getIdBankAccount();
    Integer getIdDocumentParent();
    void setIdDocumentParent(Integer idDocumentParent);
    List<Integer> getOrderIds();
}
