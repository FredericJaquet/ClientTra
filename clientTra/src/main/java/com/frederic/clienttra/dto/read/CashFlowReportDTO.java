package com.frederic.clienttra.dto.read;

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
public class CashFlowReportDTO {
    private LocalDate initDate;
    private LocalDate endDate;
    private Double grandTotalNet;
    private List<PartyReportDTO> parties;

}
