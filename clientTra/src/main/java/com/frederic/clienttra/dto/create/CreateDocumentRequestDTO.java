package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseDocumentDTO;
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
public class CreateDocumentRequestDTO implements BaseDocumentDTO {

    @NotBlank(message="validation.document.doc_number_required")
    private String docNumber;
    @NotNull(message="validation.document.doc_date_required")
    private LocalDate docDate;
    @NotBlank(message="validation.document.doc_type_required")
    private DocumentType docType;
    private DocumentStatus status=null;//No se usa en Creación, está allí simplemente para poder usar la Interfaz BasedocumentDTO

    private String language = "es";
    private Double vatRate = 0.21;
    private Double withholding = 0.15;

    private String currency = "€";

    private String noteDelivery;
    private String notePayment;
    private String noteComment;
    private LocalDate deadline;//TODO no sé si hace falta, casí que lo mejor es quitarlo de aquí y actualizar la Entity O simplemente lo omitimos en la creación.

    @NotBlank(message="validation.document.change_rate_required")
    private Integer idChangeRate;
    @NotBlank(message="validation.document.bank_account_required")
    private Integer idBankAccount;
    private Integer idDocumentParent;
    private List<Integer> orderIds;
}

