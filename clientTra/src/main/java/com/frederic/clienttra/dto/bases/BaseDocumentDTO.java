package com.frederic.clienttra.dto.bases;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public interface BaseDocumentDTO {
    String getDocNumber();
    LocalDate getDocDate();
    String getDocType();
    String getStatus();
    String getLanguage();
    Double getVatRate();
    Double getWithholding();
    Double getTotalNet();
    Double getTotalVat();
    Double getTotalGross();
    Double getTotalWithholding();
    Double getTotalToPay();
    String getCurrency();
    String getNoteDelivery();
    String getNotePayment();
    LocalDate getDeadline();
    Integer getIdChangeRate();
    Integer getIdBankAccount();
    List<Integer> getOrderIds();
}
