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

/**
 * Mapper class for converting between Document entities and various DTOs.
 * <p>
 * Supports conversions for detailed document views, list views, minimal views,
 * creation from demo DTOs, and entity updates.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class DocumentMapper {

    private final ChangeRateMapper changeRateMapper;
    private final MinimalCompanyMapper minimalCompanyMapper;
    private final BankAccountMapper bankAccountMapper;
    private final OrderMapper orderMapper;
    private final DocumentUtils documentUtils;

    /**
     * Converts a Document entity to a detailed DocumentDTO.
     * Calculates additional fields like totals in secondary currency if applicable.
     *
     * @param entity the Document entity to convert
     * @return a detailed DocumentDTO representation, or null if entity is null
     */
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
                .totalGrossInCurrency2(documentUtils.calculateTotalGrossInCurrency2(entity))
                .totalToPayInCurrency2(documentUtils.calculateTotalToPayInCurrency2(entity))
                .totalWithholding(entity.getTotalWithholding())
                .totalToPay(entity.getTotalToPay())
                .currency(entity.getCurrency())
                .noteDelivery(entity.getNoteDelivery())
                .notePayment(entity.getNotePayment())
                .noteComment(entity.getNoteComment())
                .deadline(entity.getDeadline())
                .company(minimalCompanyMapper.toBaseCompanyMinimalDTO(entity.getCompany()))
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

    /**
     * Converts a list of DocumentListProjection to a list of DocumentForListDTO.
     *
     * @param entities list of document projections
     * @return list of documents as DTOs suitable for list display
     */
    public List<DocumentForListDTO> toListDtosFromProjection(List<DocumentListProjection> entities){
        return entities.stream()
                .map(this::toListDtoFromProjection)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a DocumentListProjection to a DocumentForListDTO.
     *
     * @param entity a single document projection
     * @return a DocumentForListDTO representation
     */
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

    /**
     * Converts a Document entity to a minimal DTO with basic info.
     *
     * @param entity the Document entity
     * @return a minimal DocumentMinimalDTO
     */
    public DocumentMinimalDTO toMinimalDto(Document entity) {
        return DocumentMinimalDTO.builder()
                .idDocument(entity.getIdDocument())
                .docNumber(entity.getDocNumber())
                .docDate(entity.getDocDate())
                .build();
    }

    /**
     * Creates or updates a Document entity from a BaseDocumentDTO and associated entities.
     *
     * @param dto the source DTO with document data
     * @param changeRate associated ChangeRate entity (nullable)
     * @param bankAccount associated BankAccount entity (nullable)
     * @param documentParent parent Document entity if any (nullable)
     * @param orders list of associated Order entities (nullable)
     * @return a Document entity
     */
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
        entity.setVatRate(dto.getVatRate());
        entity.setWithholding(dto.getWithholding());
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

    /**
     * Converts a DemoDocumentDTO to a Document entity, setting owner company and defaults.
     *
     * @param dto the demo document DTO
     * @param ownerCompany the owner Company entity
     * @param company the company associated with the document
     * @return a new Document entity built from the demo DTO
     */
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

    /**
     * Updates an existing Document entity with data from a BaseDocumentDTO and associated entities.
     * Also recalculates totals using DocumentUtils.
     *
     * @param entity the Document entity to update
     * @param dto the source DTO containing new data (nullable fields allowed)
     * @param changeRate the ChangeRate entity to set
     * @param bankAccount the BankAccount entity to set
     * @param documentParent the parent Document entity to set (nullable)
     * @param orders list of Order entities to set (nullable)
     */
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
