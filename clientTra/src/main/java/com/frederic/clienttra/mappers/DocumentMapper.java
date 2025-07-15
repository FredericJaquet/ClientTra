package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseDocumentDTO;
import com.frederic.clienttra.dto.create.CreateOrderRequestDTO;
import com.frederic.clienttra.dto.demo.DemoCompanyDTO;
import com.frederic.clienttra.dto.demo.DemoDocumentDTO;
import com.frederic.clienttra.dto.read.*;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.projections.DocumentListProjection;
import com.frederic.clienttra.utils.DocumentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DocumentMapper {

    private final ChangeRateMapper changeRateMapper;
    private final BankAccountMapper bankAccountMapper;
    private final OrderMapper orderMapper;
    private final DocumentUtils documentUtils;

    public DocumentDTO toDto(Document entity) {
        if (entity == null) {
            return null;
        }

        List<OrderListDTO> orders = entity.getOrders() == null ? List.of() :
                entity.getOrders().stream()
                        .map(orderMapper::toListDtosFromEntity)
                        .collect(Collectors.toCollection(ArrayList::new));

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
                .vatRate(entity.getVatRate()*100)
                .withholding(entity.getWithholding()*100)
                .totalNet(entity.getTotalNet())
                .totalVat(entity.getTotalVat())
                .totalGross(entity.getTotalGross())
                .totalWithholding(entity.getTotalWithholding())
                .totalToPay(entity.getTotalToPay())
                .currency(entity.getCurrency())
                .noteDelivery(entity.getNoteDelivery())
                .notePayment(entity.getNotePayment())
                .noteComment(entity.getNoteComment())
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

    public List<DocumentForListDTO> toListDtosFromProjection(List<DocumentListProjection> entities){
        return entities.stream()
                .map(this::toListDtoFromProjection)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public DocumentForListDTO toListDtoFromProjection(DocumentListProjection entity){
        return DocumentForListDTO.builder()
                .idDocument(entity.getIdDocument())
                .comName(entity.getComName())
                .docNumber(entity.getDocNumber())
                .docDate(entity.getDocDate())
                .totalNet(entity.getTotalNet())
                .currency(entity.getCurrency())
                .type(entity.getDocType())
                .status(entity.getStatus())
                .build();
    }

    public DocumentMinimalDTO toMinimalDto(Document entity) {
        return DocumentMinimalDTO.builder()
                .idDocument(entity.getIdDocument())
                .docNumber(entity.getDocNumber())
                .docDate(entity.getDocDate())
                .build();
    }

    public Document toEntity(BaseDocumentDTO dto, ChangeRate changeRate, BankAccount bankAccount, Document documentParent, List<Order> orders) {
        if (dto == null) {
            return null;
        }

        Document entity = new Document();

        entity.setDocNumber(dto.getDocNumber());
        entity.setDocDate(dto.getDocDate());
        entity.setDocType(dto.getDocType());
        entity.setStatus(dto.getStatus());
        entity.setLanguage(dto.getLanguage());
        entity.setVatRate(dto.getVatRate()/100);
        entity.setWithholding(dto.getWithholding()/100);
        entity.setCurrency(dto.getCurrency());
        entity.setNoteDelivery(dto.getNoteDelivery());
        entity.setNotePayment(dto.getNotePayment());
        entity.setNoteComment(dto.getNoteComment());
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

    public Document toEntity(DemoDocumentDTO dto, Company ownerCompany, Company company){
        List<Order> orders = new ArrayList<>(orderMapper.toEntities(dto.getOrders(), ownerCompany, company));

        return Document.builder()
                .docNumber(dto.getDocNumber())
                .docDate(dto.getDocDate())
                .docType(dto.getDocType())
                .status(dto.getStatus())
                .language(dto.getLanguage())
                .vatRate(dto.getVatRate())
                .withholding(dto.getWithholding())
                .totalNet(dto.getTotalNet())
                .totalVat(dto.getTotalVat())
                .totalGross(dto.getTotalGross())
                .totalWithholding(dto.getTotalWithholding())
                .totalToPay(dto.getTotalToPay())
                .currency(dto.getCurrency())
                .noteDelivery(dto.getNoteDelivery())
                .notePayment(dto.getNotePayment())
                .noteComment(dto.getNoteComment())
                .deadline(dto.getDeadline())
                .changeRate(ownerCompany.getChangeRates().get(0))
                .bankAccount(ownerCompany.getBankAccounts().get(0))
                .orders(orders)
                .ownerCompany(ownerCompany)
                .build();
    }

    public void updateEntity(Document entity, BaseDocumentDTO dto, ChangeRate changeRate, BankAccount bankAccount, Document documentParent, List<Order> orders) {
        if (dto.getDocNumber() != null) {
            entity.setDocNumber(dto.getDocNumber());
        }
        if (dto.getDocDate() != null) {
            entity.setDocDate(dto.getDocDate());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getLanguage() != null) {
            entity.setLanguage(dto.getLanguage());
        }
        if (dto.getVatRate() != null) {
            entity.setVatRate(dto.getVatRate()/100);
        }
        if (dto.getWithholding() != null) {
            entity.setWithholding(dto.getWithholding()/100);
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
        if(dto.getNoteComment() != null){
            entity.setNoteComment(dto.getNoteComment());
        }
        if (dto.getDeadline() != null) {
            entity.setDeadline(dto.getDeadline());
        }

        entity.setChangeRate(changeRate);
        entity.setBankAccount(bankAccount);
        entity.setDocumentParent(documentParent);
        entity.setOrders(orders);
        documentUtils.calculateTotals(entity);
    }

}
