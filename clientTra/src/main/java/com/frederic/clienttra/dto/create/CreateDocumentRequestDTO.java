package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDocumentRequestDTO {

    @NotBlank(message="validation.document.doc_number_required")
    private String docNumber;
    @NotNull(message="validation.document.doc_date_required")
    private LocalDate docDate;
    @NotBlank(message="validation.document.doc_type_required")
    private DocumentType docType;
    @NotBlank(message="validation.document.status_required")
    private DocumentStatus status;//TODO Pensar si hace falta

    private String language = "es";
    private Double vatRate = 0.21;
    private Double withholding = 0.15;

    private Double totalNet = 0.0;
    private Double totalVat = 0.0;
    private Double totalGross = 0.0;
    private Double totalWithholding = 0.0;
    private Double totalToPay = 0.0;

    private String currency = "€";

    private String noteDelivery;
    private String notePayment;
    private LocalDate deadline;//TODO no sé si hace falta, casí que lo mejor es quitarlo de aquí y actualizar la Entity O simplemente lo omitimos en la creación.

    private Integer idChangeRate;
    private Integer idBankAccount;
    private Integer idDocumentParent;

    private List<Integer> orderIds;
}

