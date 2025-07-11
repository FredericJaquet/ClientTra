package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseDocumentDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDocumentRequestDTO implements BaseDocumentDTO {

    private String docNumber;
    private LocalDate docDate;
    private DocumentStatus status;
    private DocumentType docType;
    private String language;
    private Double vatRate;
    private Double withholding;

    private String currency;
    private String noteDelivery;
    private String notePayment;
    private String noteComment;
    private LocalDate deadline;

    private Integer idChangeRate;
    private Integer idBankAccount;
    private Integer idCompany;
    private Integer idDocumentParent;

    private List<Integer> orderIds;
}
