package com.frederic.clienttra.dto.demo;

import com.frederic.clienttra.dto.create.CreateBankAccountRequestDTO;
import com.frederic.clienttra.dto.create.CreateChangeRateRequestDTO;
import com.frederic.clienttra.dto.create.CreateOrderRequestDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
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
public class DemoDocumentDTO {
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
    private String noteDelivery;
    private String notePayment;
    private String noteComment;
    private LocalDate deadline;
    private CreateChangeRateRequestDTO changeRate;
    private CreateBankAccountRequestDTO bankAccount;
    private List<CreateOrderRequestDTO> orders;
}
