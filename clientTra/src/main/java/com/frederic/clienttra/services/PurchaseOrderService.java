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
 * Service implementation for handling purchase orders documents.
 * Implements CRUD and business logic related to purchase orders.
 */
@Service
@RequiredArgsConstructor
public class PurchaseOrderService implements DocumentService {

    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final BankAccountService bankAccountService;
    private final ChangeRateService changeRateService;
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final ProviderRepository providerRepository;
    private final OrderRepository orderRepository;
    private final DocumentUtils documentUtils;

    /**
     * Retrieves all documents of the specified type belonging to the current company.
     *
     * @param type DocumentType to filter by.
     * @return List of DocumentForListDTO.
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getAllDocumentsByType(DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<DocumentListProjection> entities = documentRepository.findListByDocTypeAndOwnerCompany(type, owner);
        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves all documents of the specified type and company ID,
     * belonging to the current owner company.
     *
     * @param type      DocumentType filter.
     * @param idCompany Company ID filter.
     * @return List of DocumentForListDTO.
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByCompanyId(DocumentType type, Integer idCompany) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<DocumentListProjection> entities = documentRepository.findListByDocTypeIdCompanyAndOwnerCompany(type, idCompany, owner);
        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves all documents of the specified type and status
     * belonging to the current owner company.
     *
     * @param type   DocumentType filter.
     * @param status DocumentStatus filter.
     * @return List of DocumentForListDTO.
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByStatus(DocumentType type, DocumentStatus status) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<DocumentListProjection> entities = documentRepository.findListByDocTypeStatusAndOwnerCompany(type, status, owner);
        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves all documents of specified type, company ID, and status
     * belonging to the current owner company.
     *
     * @param type      DocumentType filter.
     * @param idCompany Company ID filter.
     * @param status    DocumentStatus filter.
     * @return List of DocumentForListDTO.
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByIdCompanyAndStatus(DocumentType type, Integer idCompany, DocumentStatus status) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<DocumentListProjection> entities = documentRepository.findListByDocTypeIdCompanyStatusAndOwnerCompany(type, idCompany, status, owner);
        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves a document by its ID and type belonging to the current company.
     *
     * @param type DocumentType.
     * @param id   Document ID.
     * @return DocumentDTO representing the document.
     * @throws DocumentNotFoundException if the document does not exist.
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
     * Retrieves the last document number for a given document type
     * owned by the current company.
     *
     * @param type DocumentType.
     * @return String representing the last document number.
     * @throws LastNumberNotFoundException if no document number found.
     */
    @Transactional(readOnly = true)
    public String getLastDocumentNumber(DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        return documentRepository.findTop1DocNumberByOwnerCompanyAndDocTypeOrderByDocNumberDesc(owner, type)
                .orElseThrow(LastNumberNotFoundException::new);
    }

    /**
     * Creates a new document of the specified type for the given company,
     * calculating all necessary related data and validations.
     *
     * @param idCompany Company ID for which the document is created.
     * @param dto       BaseDocumentDTO containing the document data.
     * @param type      DocumentType.
     * @return DocumentDTO of the created document.
     * @throws CantCreateDocumentWithoutOrdersException if no orders provided.
     * @throws InvalidVatRateException                   if VAT rate is invalid.
     * @throws InvalidWithholdingException               if withholding rate is invalid.
     * @throws CustomerNotFoundException                  if provider is not found.
     * @throws CompanyNotFoundException                    if company is not found.
     * @throws DocumentNotFoundException                   if parent document is not found.
     */
    @Transactional
    @Override
    public DocumentDTO createDocument(Integer idCompany, BaseDocumentDTO dto, DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        String currency = null;
        LocalDate deadline = null;

        // Retrieve related entities
        // Retrieve related entities
        ChangeRate changeRate = dto.getIdChangeRate() != null
                ? changeRateService.getChangeRateByIdAndOwner(dto.getIdChangeRate(), owner)
                : null;
        BankAccount bankAccount = dto.getIdBankAccount() != null
                ? bankAccountService.getBankAccountByIdAndOwner(dto.getIdBankAccount(), owner)
                : null;

        if(bankAccount == null){
            bankAccount = owner.getBankAccounts().get(0);
        }

        if(changeRate == null){
            changeRate = owner.getChangeRates().get(0);
        }

        Document parent = dto.getIdDocumentParent() == null
                ? null
                : documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, dto.getIdDocumentParent(), type)
                .orElseThrow(DocumentNotFoundException::new);

        List<Order> orders = orderRepository.findAllByIdOrderInAndOwnerCompany(dto.getOrderIds(), owner);
        if (orders.isEmpty()) {
            throw new CantCreateDocumentWithoutOrdersException();
        }

        Company orderCompany = getCompany(idCompany, orders, parent);

        Company company = companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);

        // Retrieve the provider linked to the order company
        Provider provider = providerRepository.findByOwnerCompanyAndCompany(owner, orderCompany)
                .orElseThrow(CustomerNotFoundException::new);

        currency = changeRate.getCurrency1();

        // Calculate payment deadline based on document date and provider due date
        deadline = documentUtils.calculateDeadline(dto.getDocDate(), provider.getDuedate());

        // Create document entity and set calculated fields
        Document entity = documentMapper.toEntity(dto, changeRate, bankAccount, parent, orders);
        entity.setOwnerCompany(owner);
        entity.setCompany(company);
        entity.setCurrency(currency);
        entity.setDeadline(deadline);
        entity.setDocType(type);

        // Calculate document totals (net, VAT, total, etc.)
        documentUtils.calculateTotals(entity);

        // Save and return the created document
        Document newEntity = documentRepository.save(entity);

        return documentMapper.toDto(newEntity);
    }

    /**
     * Updates an existing document by its ID and type,
     * applying changes from the provided DTO.
     *
     * @param idDocument Document ID to update.
     * @param dto        BaseDocumentDTO with updated data.
     * @param type       DocumentType.
     * @return Updated DocumentDTO.
     * @throws DocumentNotFoundException if document is not found.
     * @throws InvalidVatRateException   if VAT rate is invalid.
     * @throws InvalidWithholdingException if withholding rate is invalid.
     */
    @Transactional
    @Override
    public DocumentDTO updateDocument(Integer idDocument, BaseDocumentDTO dto, DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Document entity = documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, idDocument, type)
                .orElseThrow(DocumentNotFoundException::new);

        Company orderCompany = entity.getCompany();

        Provider provider = providerRepository.findByOwnerCompanyAndCompany(owner, orderCompany)
                .orElseThrow(CustomerNotFoundException::new);

        ChangeRate changeRate;

        List<Order> orders;

        // If no new change rate provided, use existing
        if (dto.getIdChangeRate() == null) {
            changeRate = entity.getChangeRate();
            entity.setCurrency(changeRate.getCurrency1());
        } else {
            changeRate = changeRateService.getChangeRateByIdAndOwner(dto.getIdChangeRate(), owner);
            entity.setCurrency(changeRate.getCurrency1());
        }

        // Update bank account if provided
        if (dto.getIdBankAccount() != null) {
            BankAccount bankAccount = bankAccountService.getBankAccountByIdAndOwner(dto.getIdBankAccount(), owner);
            entity.setBankAccount(bankAccount);
        }

        // Update orders list if provided
        if (dto.getOrderIds() != null && !dto.getOrderIds().isEmpty()) {
            orders = orderRepository.findAllByIdOrderInAndOwnerCompany(dto.getOrderIds(), owner);
            entity.setOrders(orders);
        }

        // Update deadline if document date provided
        if (dto.getDocDate() != null) {
            LocalDate deadline = documentUtils.calculateDeadline(dto.getDocDate(), provider.getDuedate());
            entity.setDeadline(deadline);
        }

        // Validate rates if provided
        if (dto.getVatRate() != null && dto.getVatRate() < 1) {
            throw new InvalidVatRateException();
        }
        if (dto.getWithholding() != null && dto.getWithholding() < 1) {
            throw new InvalidWithholdingException();
        }

        // Update entity fields from DTO
        documentMapper.updateEntity(entity, dto, entity.getChangeRate(), entity.getBankAccount(), null, entity.getOrders());

        Document newEntity = documentRepository.save(entity);

        return documentMapper.toDto(newEntity);
    }

    /**
     * Performs a soft delete on the document, marking its status as DELETED.
     *
     * @param id Document ID to soft delete.
     * @throws DocumentNotFoundException if document does not exist.
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
     * Utility method to validate that all orders belong to the specified company
     * and are not already billed in another active document (unless it's the parent).
     *
     * @param idCompany Company ID to check against.
     * @param orders    List of orders involved.
     * @param parent    Optional parent document for modification scenarios.
     * @return Company of the first order (assuming all same).
     * @throws CantIncludeOrderAlreadyBilledException if an order is already billed and not part of the parent.
     * @throws OrderDoNotBelongToCompanyException     if any order belongs to a different company.
     */
    private static Company getCompany(Integer idCompany, List<Order> orders, Document parent) {
        Company orderCompany = orders.get(0).getCompany();

        for (Order order : orders) {
            // Check if the order is already billed
            if (order.getBilled() != null && order.getBilled()) {
                // Check if order belongs to the parent document in case of modification
                boolean isPartOfModifiedInvoice = parent != null && parent.getOrders().contains(order);

                if (!isPartOfModifiedInvoice) {
                    throw new CantIncludeOrderAlreadyBilledException();
                }
            }
            // Verify that the order belongs to the given company
            if (!Objects.equals(orderCompany.getIdCompany(), idCompany)) {
                throw new OrderDoNotBelongToCompanyException();
            }
        }
        return orderCompany;
    }
}
