package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseDocumentDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO used to create a new financial document (invoice, quote, purchase order, etc.).
 * Includes metadata such as document number, type, date, and financial details like VAT, currency, and withholding.
 * Also supports linking orders and optional parent document references.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDocumentRequestDTO implements BaseDocumentDTO {

    @NotBlank(message="validation.document.doc_number_required")
    private String docNumber;
    @NotNull(message="validation.document.doc_date_required")
    private LocalDate docDate;
    private DocumentType docType;
    private DocumentStatus status=null; // Not used on creation, it is here just to use the BaseDocumentDTO interface

    private String language = "es";
    private Double vatRate = 0.21;
    private Double withholding = 0.15;

    private String currency = "€";

    private String noteDelivery;
    private String notePayment;
    private String noteComment;
    private LocalDate deadline;

    private Integer idChangeRate;
    private Integer idBankAccount;
    private Integer idDocumentParent;
    private List<Integer> orderIds;
}
