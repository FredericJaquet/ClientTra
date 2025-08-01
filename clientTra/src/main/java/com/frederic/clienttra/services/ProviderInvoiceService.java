package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.bases.BaseDocumentDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.exceptions.*;
import com.frederic.clienttra.mappers.DocumentMapper;
import com.frederic.clienttra.projections.DocumentListProjection;
import com.frederic.clienttra.repositories.*;
import com.frederic.clienttra.utils.DocumentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Service class to manage provider invoices.
 * Implements CRUD operations and business logic for provider documents.
 */
@Service
@RequiredArgsConstructor
public class ProviderInvoiceService implements DocumentService {

    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final BankAccountService bankAccountService;
    private final ChangeRateService changeRateService;
    private final CompanyService companyService;
    private final OrderService orderService;
    private final CompanyRepository companyRepository;
    private final ProviderRepository providerRepository;
    private final OrderRepository orderRepository;
    private final DocumentUtils documentUtils;

    /**
     * Retrieves all documents filtered by document type for the current owner company.
     *
     * @param type the document type.
     * @return list of documents DTO for the given type.
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getAllDocumentsByType(DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<DocumentListProjection> entities = documentRepository.findListByDocTypeAndOwnerCompany(type, owner);

        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves documents by document type and company ID.
     *
     * @param type the document type.
     * @param idCompany the company ID.
     * @return list of documents DTO.
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByCompanyId(DocumentType type, Integer idCompany) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<DocumentListProjection> entities = documentRepository.findListByDocTypeIdCompanyAndOwnerCompany(type, idCompany, owner);

        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves documents by document type and status.
     *
     * @param type the document type.
     * @param status the document status.
     * @return list of documents DTO.
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByStatus(DocumentType type, DocumentStatus status) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<DocumentListProjection> entities = documentRepository.findListByDocTypeStatusAndOwnerCompany(type, status, owner);

        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves documents by document type, company ID and status.
     *
     * @param type the document type.
     * @param idCompany the company ID.
     * @param status the document status.
     * @return list of documents DTO.
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByIdCompanyAndStatus(DocumentType type, Integer idCompany, DocumentStatus status) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<DocumentListProjection> entities = documentRepository.findListByDocTypeIdCompanyStatusAndOwnerCompany(type, idCompany, status, owner);
        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves a document by its ID and type.
     *
     * @param type the document type.
     * @param id the document ID.
     * @return the document DTO.
     * @throws DocumentNotFoundException if the document is not found.
     */
    @Transactional(readOnly = true)
    @Override
    public DocumentDTO getDocumentById(DocumentType type, Integer id) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Document entity = documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, id, type)
                .orElseThrow(DocumentNotFoundException::new);

        return documentMapper.toDto(entity);
    }

    /**
     * Creates a new provider invoice document.
     *
     * @param idCompany the company ID.
     * @param dto the base document DTO containing data.
     * @param type the document type.
     * @return the created document DTO.
     * @throws CantCreateDocumentWithoutOrdersException if no orders are linked.
     * @throws InvalidVatRateException if VAT rate is invalid.
     * @throws InvalidWithholdingException if withholding percentage is invalid.
     * @throws CantIncludeOrderAlreadyBilledException if an order is already billed on a different invoice.
     * @throws OrderDoNotBelongToCompanyException if orders belong to a different company.
     */
    @Transactional
    @Override
    public DocumentDTO createDocument(Integer idCompany, BaseDocumentDTO dto, DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String currency = null;
        LocalDate deadline = null;

        // 1. Retrieve related entities
        ChangeRate changeRate = changeRateService.getChangeRateByIdAndOwner(dto.getIdChangeRate(), owner);
        BankAccount bankAccount = dto.getIdBankAccount() != null ? bankAccountService.getBankAccountByIdAndOwner(dto.getIdBankAccount(), owner) : null;
        Document parent = dto.getIdDocumentParent() == null ? null : documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, dto.getIdDocumentParent(), type)
                .orElseThrow(DocumentNotFoundException::new);
        List<Order> orders = orderRepository.findAllByIdOrderInAndOwnerCompany(dto.getOrderIds(), owner);
        if (orders.isEmpty()) {
            throw new CantCreateDocumentWithoutOrdersException();
        }

        // Validate percentages
        if (dto.getVatRate() < 1) {
            throw new InvalidVatRateException();
        }
        if (dto.getWithholding() < 1) {
            throw new InvalidWithholdingException();
        }

        Company orderCompany = getCompany(idCompany, orders, parent);

        Company company = companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);

        // 2. Calculate fields
        Provider provider = providerRepository.findByOwnerCompanyAndCompany(owner, orderCompany)
                .orElseThrow(ProviderNotFoundException::new);

        currency = changeRate.getCurrency1();
        deadline = documentUtils.calculateDeadline(dto.getDocDate(), provider.getDuedate());

        // 3. Create entity
        Document entity = documentMapper.toEntity(dto, changeRate, bankAccount, parent, orders);
        entity.setOwnerCompany(owner);
        entity.setCompany(company);
        entity.setCurrency(currency);
        entity.setDeadline(deadline);
        entity.setDocType(type);

        // 4. Calculate totals
        documentUtils.calculateTotals(entity);

        // 5. Mark orders as billed before saving the document
        orderService.markOrdersAsBilled(orders);

        // 6. Save the document
        Document newEntity = documentRepository.save(entity);

        return documentMapper.toDto(newEntity);
    }

    /**
     * Updates an existing provider invoice document.
     * If the document is already modified or paid, restrictions apply.
     *
     * @param idDocument the document ID.
     * @param dto the update DTO.
     * @param type the document type.
     * @return the updated document DTO.
     * @throws CantModifyDocumentAlreadyModified if document status is MODIFIED.
     * @throws CantModifyPaidInvoiceException if invoice is paid.
     * @throws DocumentNotFoundException if document is not found.
     */
    @Transactional
    @Override
    public DocumentDTO updateDocument(Integer idDocument, BaseDocumentDTO dto, DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Document entityParent = documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, idDocument, type)
                .orElseThrow(DocumentNotFoundException::new);

        if (entityParent.getStatus().equals(DocumentStatus.MODIFIED)) {
            throw new CantModifyDocumentAlreadyModified();
        }

        if (entityParent.getDocType().equals(DocumentType.INV_PROV)) {
            if (!entityParent.getStatus().equals(DocumentStatus.PENDING)) {
                throw new CantModifyPaidInvoiceException();
            }
            entityParent.setStatus(DocumentStatus.MODIFIED);
            documentRepository.save(entityParent);
        }

        dto.setIdDocumentParent(entityParent.getIdDocument());
        Integer idCompany = entityParent.getCompany().getIdCompany();

        return createDocument(idCompany, dto, DocumentType.INV_PROV);
    }

    /**
     * Soft deletes a document by setting its status to DELETED.
     *
     * @param id the document ID.
     * @throws DocumentNotFoundException if document is not found.
     */
    @Transactional
    @Override
    public void softDeleteDocument(Integer id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Document entity = documentRepository.findByOwnerCompanyAndIdDocument(owner, id)
                .orElseThrow(DocumentNotFoundException::new);
        entity.setStatus(DocumentStatus.DELETED);
        documentRepository.save(entity);
    }

    /**
     * Helper method to verify orders belong to the company and are not already billed elsewhere.
     *
     * @param idCompany the expected company ID.
     * @param orders list of orders linked to the document.
     * @param parent parent document if updating.
     * @return the company of the orders.
     * @throws CantIncludeOrderAlreadyBilledException if an order is billed in another invoice.
     * @throws OrderDoNotBelongToCompanyException if orders belong to different company.
     */
    private static Company getCompany(Integer idCompany, List<Order> orders, Document parent) {
        Company orderCompany = orders.get(0).getCompany();

        for (Order order : orders) {
            // Check if order is already linked to another active invoice
            if (order.getBilled() != null && order.getBilled()) {
                // Check if the order is part of the parent modified invoice
                boolean isPartOfModifiedInvoice = parent != null && parent.getOrders().contains(order);

                if (!isPartOfModifiedInvoice) {
                    throw new CantIncludeOrderAlreadyBilledException();
                }
            }
            if (!Objects.equals(orderCompany.getIdCompany(), idCompany)) {
                throw new OrderDoNotBelongToCompanyException();
            }
        }
        return orderCompany;
    }
}
