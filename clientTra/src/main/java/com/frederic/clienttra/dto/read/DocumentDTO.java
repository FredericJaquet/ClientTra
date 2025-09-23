package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object representing detailed information of a document.
 * Includes identification, dates, types, financial totals, notes, deadlines,
 * associated change rate, bank account, parent document, and related orders.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDTO {

    private Integer idDocument;
    private String docNumber;
    private LocalDate docDate;

    private DocumentType docType;
    private DocumentStatus status;
    private String language;
    private Double vatRate;
    private Double withholding;
    private Double totalNet;
    private Double totalVat;
    private Double totalGross;
    private Double totalWithholding;
    private Double totalToPay;
    private String currency;

    private Double totalGrossInCurrency2;
    private Double totalToPayInCurrency2;

    private String noteDelivery;
    private String notePayment;
    private String noteComment;
    private LocalDate deadline;
    private BaseCompanyMinimalDTO company;
    private ChangeRateDTO changeRate;
    private BankAccountDTO bankAccount;
    private DocumentMinimalDTO documentParent;

    private List<OrderListDTO> orders;
}
