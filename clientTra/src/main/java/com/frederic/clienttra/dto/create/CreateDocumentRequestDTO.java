package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseBankAccountDTO;
import com.frederic.clienttra.dto.bases.BaseChangeRateDTO;
import com.frederic.clienttra.dto.bases.BaseDocumentDTO;
import com.frederic.clienttra.dto.bases.BaseOrderDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDocumentRequestDTO implements BaseDocumentDTO{

    @NotBlank(message="validation.document.doc_number_required")
    private String docNumber;
    @NotNull(message="validation.document.doc_date_required")
    private LocalDate docDate;
    @NotBlank(message="validation.document.doc_type_required")
    private String docType;
    @NotBlank(message="validation.document.status_required")
    private String status;
    private String language="es";
    private Double vatRate=0.21;
    private Double withholding=0.15;
    private Double totalNet=0.0;
    private Double totalVat=totalNet*vatRate;
    private Double totalGross=totalNet+totalVat;
    private Double totalWithholding=totalNet*withholding;
    private Double totalToPay=totalGross-totalWithholding;
    private String currency="€";
    private String noteDelivery;
    private String notePayment;
    private LocalDate deadline;
    private BaseChangeRateDTO changeRate;
    private BaseBankAccountDTO bankAccount;
    private BaseDocumentDTO documentParent;
    private List<BaseOrderDTO> orders;

}
