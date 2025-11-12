package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.bases.BaseDocumentDTO;
import com.frederic.clienttra.dto.read.BankAccountDTO;
import com.frederic.clienttra.dto.read.ChangeRateDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.exceptions.*;
import com.frederic.clienttra.mappers.BankAccountMapper;
import com.frederic.clienttra.mappers.ChangeRateMapper;
import com.frederic.clienttra.mappers.DocumentMapper;
import com.frederic.clienttra.projections.DocumentListProjection;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.repositories.CustomerRepository;
import com.frederic.clienttra.repositories.DocumentRepository;
import com.frederic.clienttra.repositories.OrderRepository;
import com.frederic.clienttra.utils.DocumentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Objects;

/**
 * Service implementation for managing quote (presupuesto) documents.
 * Implements the DocumentService interface for CRUD and business logic.
 */
@Service
@RequiredArgsConstructor
public class QuoteService implements DocumentService {

    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final BankAccountService bankAccountService;
    private final BankAccountMapper bankAccountMapper;
    private final ChangeRateService changeRateService;
    private final ChangeRateMapper changeRateMapper;
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final DocumentUtils documentUtils;

    /**
     * Retrieves all documents of a given type belonging to the current company.
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
     * Retrieves all documents of a given type for a specific company ID,
     * filtered by the current owner company.
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
     * Retrieves all documents of a given type and status,
     * filtered by the current owner company.
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
     * Retrieves all documents filtered by type, company ID, and status,
     * within the current owner company.
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
     * Retrieves a document by its ID and type within the current company.
     *
     * @param type DocumentType.
     * @param id   Document ID.
     * @return DocumentDTO representing the document.
     * @throws DocumentNotFoundException if no document found.
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
     * Retrieves the last document number used for a specific document type.
     *
     * @param type DocumentType.
     * @return String last document number.
     * @throws LastNumberNotFoundException if no document numbers found.
     */
    @Transactional(readOnly = true)
    public String getLastDocumentNumber(DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String yearPrefix = "Q"+Year.now().toString() + "-";
        String lastNumber;

        lastNumber = documentRepository
                .findDocNumbersByOwnerCompanyAndDocTypeAndDocNumberStartingWith(owner, type, yearPrefix, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);

        if(lastNumber == null){
            yearPrefix = "Q"+(Year.now().minusYears(1)).toString() + "-";
            lastNumber = documentRepository
                    .findDocNumbersByOwnerCompanyAndDocTypeAndDocNumberStartingWith(owner, type, yearPrefix, PageRequest.of(0, 1))
                    .stream()
                    .findFirst()
                    .orElse("N/A");
        }

        return lastNumber;
    }

    /**
     * Creates a new quote document for the specified company.
     * Validates orders, rates, and calculates totals and deadlines.
     *
     * @param idCompany Company ID.
     * @param dto       BaseDocumentDTO with input data.
     * @param type      DocumentType.
     * @return DocumentDTO of the newly created quote.
     * @throws CantCreateDocumentWithoutOrdersException if no orders given.
     * @throws InvalidVatRateException                   if VAT rate invalid.
     * @throws InvalidWithholdingException               if withholding invalid.
     * @throws CustomerNotFoundException                  if customer not found.
     * @throws CompanyNotFoundException                    if company not found.
     * @throws DocumentNotFoundException                   if parent document missing.
     */
    @Transactional
    @Override
    public DocumentDTO createDocument(Integer idCompany, BaseDocumentDTO dto, DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String currency = null;
        LocalDate deadline = null;

        System.out.println(dto.getIdBankAccount());

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

        // Retrieve customer linked to the order company
        Customer customer = customerRepository.findByOwnerCompanyAndCompany(owner, orderCompany)
                .orElseThrow(CustomerNotFoundException::new);

        currency = changeRate.getCurrency1();

        // Calculate payment deadline based on document date and customer's due date
        deadline = documentUtils.calculateDeadline(dto.getDocDate(), customer.getDuedate());

        // Create the document entity
        Document entity = documentMapper.toEntity(dto, changeRate, bankAccount, parent, orders);
        entity.setOwnerCompany(owner);
        entity.setCompany(company);
        entity.setCurrency(currency);
        entity.setDeadline(deadline);
        entity.setDocType(type);

        // Calculate totals (net, VAT, total amounts)
        documentUtils.calculateTotals(entity);

        // Persist and return the created document DTO
        Document newEntity = documentRepository.save(entity);
        return documentMapper.toDto(newEntity);
    }

    /**
     * Updates an existing quote document with new data.
     * Validates and recalculates totals and deadlines accordingly.
     *
     * @param idDocument Document ID.
     * @param dto        BaseDocumentDTO with update data.
     * @param type       DocumentType.
     * @return Updated DocumentDTO.
     * @throws DocumentNotFoundException if document not found.
     * @throws InvalidVatRateException   if VAT rate invalid.
     * @throws InvalidWithholdingException if withholding invalid.
     */
    @Transactional
    @Override
    public DocumentDTO updateDocument(Integer idDocument, BaseDocumentDTO dto, DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Document entity = documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, idDocument, type)
                .orElseThrow(DocumentNotFoundException::new);

        Company orderCompany = entity.getCompany();

        Customer customer = customerRepository.findByOwnerCompanyAndCompany(owner, orderCompany)
                .orElseThrow(CustomerNotFoundException::new);

        ChangeRate changeRate;
        List<Order> orders;

        // Determine change rate to apply
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

        // Update orders if provided
        if (dto.getOrderIds() != null && !dto.getOrderIds().isEmpty()) {
            orders = orderRepository.findAllByIdOrderInAndOwnerCompany(dto.getOrderIds(), owner);
            entity.setOrders(orders);
        }

        // Update deadline if document date provided
        if (dto.getDocDate() != null) {
            LocalDate deadline = documentUtils.calculateDeadline(dto.getDocDate(), customer.getDuedate());
            entity.setDeadline(deadline);
        }

        // Update the entity from DTO fields
        documentMapper.updateEntity(entity, dto, entity.getChangeRate(), entity.getBankAccount(), null, entity.getOrders());

        Document newEntity = documentRepository.save(entity);
        return documentMapper.toDto(newEntity);
    }

    /**
     * Marks a document as deleted by setting its status to DELETED (soft delete).
     *
     * @param id Document ID.
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
     * Validates that all orders belong to the specified company,
     * and that none are already billed unless they belong to the parent document.
     *
     * @param idCompany Company ID to validate.
     * @param orders    List of orders.
     * @param parent    Parent document for modification scenarios (nullable).
     * @return Company of the first order.
     * @throws CantIncludeOrderAlreadyBilledException if order is billed elsewhere.
     * @throws OrderDoNotBelongToCompanyException     if orders belong to a different company.
     */
    private static Company getCompany(Integer idCompany, List<Order> orders, Document parent) {
        Company orderCompany = orders.get(0).getCompany();

        for (Order order : orders) {
            // Check if order is already billed in another active document
            if (order.getBilled() != null && order.getBilled()) {
                // Check if order belongs to the parent document in case of editing
                boolean isPartOfModifiedInvoice = parent != null && parent.getOrders().contains(order);

                if (!isPartOfModifiedInvoice) {
                    throw new CantIncludeOrderAlreadyBilledException();
                }
            }
            // Verify orders belong to the specified company
            if (!Objects.equals(orderCompany.getIdCompany(), idCompany)) {
                throw new OrderDoNotBelongToCompanyException();
            }
        }
        return orderCompany;
    }
}
