package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.enums.DocumentStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDocumentRequestDTO {

    private String docNumber;
    private LocalDate docDate;
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
    private String noteDelivery;
    private String notePayment;
    private LocalDate deadline;

    private Integer idChangeRate;
    private Integer idBankAccount;

    private List<Integer> orderIds;
}
