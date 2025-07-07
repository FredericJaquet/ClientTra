package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.read.*;
import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.update.UpdateDocumentRequestDTO;
import com.frederic.clienttra.entities.BankAccount;
import com.frederic.clienttra.entities.ChangeRate;
import com.frederic.clienttra.entities.Document;
import com.frederic.clienttra.entities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DocumentMapper {

    private final ChangeRateMapper changeRateMapper;
    private final BankAccountMapper bankAccountMapper;
    private final OrderMapper orderMapper;

    public DocumentDTO toDto(Document entity) {
        if (entity == null) {
            return null;
        }

        List<OrderListDTO> orders = entity.getOrders() == null ? List.of() :
                entity.getOrders().stream()
                        .map(orderMapper::toListDto)
                        .collect(Collectors.toList());

        DocumentMinimalDTO parentDto = null;
        if (entity.getDocumentParent() != null) {
            parentDto = toMinimalDto(entity.getDocumentParent());
        }

        DocumentDTO dto = DocumentDTO.builder()
                .idDocument(entity.getIdDocument())
                .docNumber(entity.getDocNumber())
                .docDate(entity.getDocDate())
                .docType(entity.getDocType())
                .status(entity.getStatus())
                .language(entity.getLanguage())
                .vatRate(entity.getVatRate())
                .withholding(entity.getWithholding())
                .totalNet(entity.getTotalNet())
                .totalVat(entity.getTotalVat())
                .totalGross(entity.getTotalGross())
                .totalWithholding(entity.getTotalWithholding())
                .totalToPay(entity.getTotalToPay())
                .currency(entity.getCurrency())
                .noteDelivery(entity.getNoteDelivery())
                .notePayment(entity.getNotePayment())
                .deadline(entity.getDeadline())
                .changeRate(changeRateMapper.toDto(entity.getChangeRate()))
                .bankAccount(bankAccountMapper.toBankAccountDTO(entity.getBankAccount()))
                .documentParent(parentDto)
                .orders(orders)
                .build();

        if (entity.getChangeRate() != null && entity.getChangeRate().getIdChangeRate() != 1) {
            Double rate = entity.getChangeRate().getRate();
            if (rate != null) {
                dto.setTotalGrossInCurrency2(entity.getTotalGross() * rate);
                dto.setTotalToPayInCurrency2(entity.getTotalToPay() * rate);
            }
        }

        return dto;
    }

    public DocumentMinimalDTO toMinimalDto(Document entity) {
        return DocumentMinimalDTO.builder()
                .idDocument(entity.getIdDocument())
                .docNumber(entity.getDocNumber())
                .docDate(entity.getDocDate())
                .build();
    }

    public Document toEntity(CreateDocumentRequestDTO dto, ChangeRate changeRate, BankAccount bankAccount, Document documentParent, List<Order> orders) {
        if (dto == null) {
            return null;
        }

        Document entity = new Document();

        entity.setDocNumber(dto.getDocNumber());
        entity.setDocDate(dto.getDocDate());
        entity.setDocType(dto.getDocType());   // enum
        entity.setStatus(dto.getStatus());     // enum
        entity.setLanguage(dto.getLanguage());
        entity.setVatRate(dto.getVatRate());
        entity.setWithholding(dto.getWithholding());
        entity.setTotalNet(dto.getTotalNet());
        entity.setTotalVat(dto.getTotalVat());
        entity.setTotalGross(dto.getTotalGross());
        entity.setTotalWithholding(dto.getTotalWithholding());
        entity.setTotalToPay(dto.getTotalToPay());
        entity.setCurrency(dto.getCurrency());
        entity.setNoteDelivery(dto.getNoteDelivery());
        entity.setNotePayment(dto.getNotePayment());
        entity.setDeadline(dto.getDeadline());

        if (changeRate != null) {
            entity.setChangeRate(changeRate);
        }
        if (bankAccount != null) {
            entity.setBankAccount(bankAccount);
        }
        if (documentParent != null) {
            entity.setDocumentParent(documentParent);
        }
        if (orders != null) {
            entity.setOrders(orders);
        }

        return entity;
    }

    public void updateEntity(Document entity, UpdateDocumentRequestDTO dto, ChangeRate changeRate, BankAccount bankAccount, Document documentParent, List<Order> orders) {//Usamos un CreateDocumentRequestDTO, porque no vamos a actualizar, sino crear un documento nuevo
        if (dto.getDocNumber() != null) {
            entity.setDocNumber(dto.getDocNumber());
        }
        if (dto.getDocDate() != null) {
            entity.setDocDate(dto.getDocDate());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());     // enum
        }
        if (dto.getLanguage() != null) {
            entity.setLanguage(dto.getLanguage());
        }
        if (dto.getVatRate() != null) {
            entity.setVatRate(dto.getVatRate());
        }
        if (dto.getWithholding() != null) {
            entity.setWithholding(dto.getWithholding());
        }
        if (dto.getTotalNet() != null) {
            entity.setTotalNet(dto.getTotalNet());
        }
        if (dto.getTotalVat() != null) {
            entity.setTotalVat(dto.getTotalVat());
        }
        if (dto.getTotalGross() != null) {
            entity.setTotalGross(dto.getTotalGross());
        }
        if (dto.getTotalWithholding() != null) {
            entity.setTotalWithholding(dto.getTotalWithholding());
        }
        if (dto.getTotalToPay() != null) {
            entity.setTotalToPay(dto.getTotalToPay());
        }
        if (dto.getCurrency() != null) {
            entity.setCurrency(dto.getCurrency());
        }
        if (dto.getNoteDelivery() != null) {
            entity.setNoteDelivery(dto.getNoteDelivery());
        }
        if (dto.getNotePayment() != null) {
            entity.setNotePayment(dto.getNotePayment());
        }
        if (dto.getDeadline() != null) {
            entity.setDeadline(dto.getDeadline());
        }

        entity.setChangeRate(changeRate);
        entity.setBankAccount(bankAccount);
        entity.setDocumentParent(documentParent);
        entity.setOrders(orders);
    }


}
