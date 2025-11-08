package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.bases.BaseDocumentDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.dto.read.ItemDTO;
import com.frederic.clienttra.dto.read.OrderForDocumentDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.exceptions.*;
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
import java.util.Optional;

/**
 * Service implementation to manage customer invoices and related document operations.
 * <p>
 * This service supports CRUD operations for customer invoice documents including
 * creation, updating, retrieval, listing by various filters, and soft deletion.
 * It enforces ownership, validates business rules related to orders and customers,
 * and calculates financial fields such as totals and deadlines.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomerInvoiceService implements DocumentService {

    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final BankAccountService bankAccountService;
    private final ChangeRateService changeRateService;
    private final CompanyService companyService;
    private final OrderService orderService;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final DocumentUtils documentUtils;

    /**
     * Retrieves all documents of a specified type belonging to the current user's company.
     *
     * @param type the type of documents to retrieve
     * @return a list of document DTOs for display
     * @throws CompanyNotFoundException if the current user's company is not found
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getAllDocumentsByType(DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<DocumentListProjection> entities = documentRepository.findListByDocTypeAndOwnerCompany(type, owner);
        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves all documents of a specified type for a given company.
     *
     * @param type      the document type
     * @param idCompany the ID of the company whose documents are fetched
     * @return a list of document DTOs
     * @throws CompanyNotFoundException if the specified company does not exist or does not belong to the current user
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByCompanyId(DocumentType type, Integer idCompany) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<DocumentListProjection> entities = documentRepository.findListByDocTypeIdCompanyAndOwnerCompany(type, idCompany, owner);
        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves documents filtered by status and type for the current company.
     *
     * @param type   the document type
     * @param status the document status filter
     * @return a list of document DTOs matching the filters
     * @throws CompanyNotFoundException if the current user's company is not found
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByStatus(DocumentType type, DocumentStatus status) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<DocumentListProjection> entities = documentRepository.findListByDocTypeStatusAndOwnerCompany(type, status, owner);
        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves documents for a specific company and status.
     *
     * @param type      the document type
     * @param idCompany the company ID
     * @param status    the document status
     * @return a filtered list of document DTOs
     * @throws CompanyNotFoundException if the specified company does not exist or is not owned by the current user
     */
    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByIdCompanyAndStatus(DocumentType type, Integer idCompany, DocumentStatus status) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<DocumentListProjection> entities = documentRepository.findListByDocTypeIdCompanyStatusAndOwnerCompany(type, idCompany, status, owner);
        return documentMapper.toListDtosFromProjection(entities);
    }

    /**
     * Retrieves a single document by its ID and type.
     *
     * @param type the document type
     * @param id   the document ID
     * @return the document DTO
     * @throws DocumentNotFoundException if the document does not exist or does not belong to the current user's company
     */
    @Transactional(readOnly = true)
    @Override
    public DocumentDTO getDocumentById(DocumentType type, Integer id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Document entity = documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, id, type)
                .orElseThrow(DocumentNotFoundException::new);
        DocumentDTO dto = documentMapper.toDto(entity);

        //Calculate totals in currency2
        Double rate = entity.getChangeRate().getRate();
        Double totalGross2 = entity.getTotalGross()*rate;
        Double totalToPay2 = entity.getTotalToPay()*rate;

        dto.setTotalGrossInCurrency2(totalGross2);
        dto.setTotalToPayInCurrency2(totalToPay2);

        // Calculate orders quantity
        List<OrderForDocumentDTO> orders=dto.getOrders();

        for(OrderForDocumentDTO order : orders){
            double quantity=0.0;
            for(ItemDTO item : order.getItems()){
                quantity=quantity + item.getQty();
            }
            order.setQuantity(quantity);
        }

        return dto;
    }

    /**
     * Retrieves the last (highest) document number for a given document type.
     *
     * @param type the document type
     * @return the last document number as String
     * @throws LastNumberNotFoundException if no documents exist for the current user's company and document type
     */
    @Transactional(readOnly = true)
    public String getLastDocumentNumber(DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String lastNumber;
        String yearPrefix = Year.now().toString() + "-";

        System.out.println("CustomerInvoiceService linea 169: "+yearPrefix);

        lastNumber = documentRepository
                .findDocNumbersByOwnerCompanyAndDocTypeAndDocNumberStartingWith(owner, type, yearPrefix, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);

        if(lastNumber == null){
            yearPrefix = (Year.now().minusYears(1)).toString() + "-";
            lastNumber = documentRepository
                    .findDocNumbersByOwnerCompanyAndDocTypeAndDocNumberStartingWith(owner, type, yearPrefix, PageRequest.of(0, 1))
                    .stream()
                    .findFirst()
                    .orElse("N/A");
        }

        return lastNumber;
    }

    /**
     * Creates a new document (customer invoice) for a specified company.
     * <p>
     * Validates orders, related entities, VAT and withholding rates, and assigns
     * calculated fields such as payment note, currency, deadline, and totals.
     * Also marks related orders as billed.
     * </p>
     *
     * @param idCompany the ID of the company to which the document belongs
     * @param dto       the base document DTO containing document details
     * @param type      the type of document to create
     * @return the created document DTO
     * @throws CompanyNotFoundException              if the company does not exist or is not owned by the user
     * @throws CustomerNotFoundException             if the customer associated with the company is not found
     * @throws CantCreateDocumentWithoutOrdersException if no orders are specified
     * @throws InvalidVatRateException               if the VAT rate is less than 1%
     * @throws InvalidWithholdingException           if the withholding rate is less than 1%
     * @throws DocumentNotFoundException             if the parent document is not found when specified
     * @throws CantIncludeOrderAlreadyBilledException if any order has already been billed in another active document
     * @throws OrderDoNotBelongToCompanyException     if any order does not belong to the specified company
     */
    @Transactional
    @Override
    public DocumentDTO createDocument(Integer idCompany, BaseDocumentDTO dto, DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String notePayment = null;
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

        // Validate VAT and withholding rates
        if (dto.getVatRate() > 1) {
            dto.setVatRate(dto.getVatRate()/100);
        }
        if (dto.getWithholding() > 1) {
            dto.setWithholding(dto.getWithholding()/100);
        }

        Company orderCompany = getCompany(idCompany, orders, parent);

        Company company = companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);

        // 2. Calculate derived fields
        Customer customer = customerRepository.findByOwnerCompanyAndCompany(owner, orderCompany)
                .orElseThrow(CustomerNotFoundException::new);

        notePayment = documentUtils.generateNotePayment(dto.getDocDate(), customer, bankAccount);

        currency = changeRate.getCurrency1();
        deadline = documentUtils.calculateDeadline(dto.getDocDate(), customer.getDuedate());

        // 3. Create entity
        Document entity = documentMapper.toEntity(dto, changeRate, bankAccount, parent, orders);
        entity.setOwnerCompany(owner);
        entity.setCompany(company);
        entity.setNotePayment(notePayment);
        entity.setCurrency(currency);
        entity.setDeadline(deadline);
        entity.setDocType(type);
        //entity.setStatus(DocumentStatus.PENDING);

        // 4. Calculate totals
        documentUtils.calculateTotals(entity);

        // 5. Mark orders as billed before saving the document
        orderService.markOrdersAsBilled(orders);

        // 6. Save the document
        Document newEntity = documentRepository.save(entity);

        return documentMapper.toDto(newEntity);
    }

    /**
     * Updates an existing document by creating a new modified document version.
     * <p>
     * Marks the original document as MODIFIED to prevent further modifications.
     * Only documents of type {@link DocumentType#INV_CUST} with PENDING status
     * can be modified.
     * </p>
     *
     * @param idDocument the ID of the document to update
     * @param dto        the updated document data
     * @param type       the type of document (should be customer invoice)
     * @return the new modified document DTO
     * @throws DocumentNotFoundException          if the document does not exist or does not belong to the user
     * @throws CantModifyDocumentAlreadyModified  if the document is already modified
     * @throws CantModifyPaidInvoiceException     if the invoice has already been paid and thus cannot be modified
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

        if (entityParent.getDocType().equals(DocumentType.INV_CUST)) {
            if (!entityParent.getStatus().equals(DocumentStatus.PENDING)) {
                throw new CantModifyPaidInvoiceException();
            }
            entityParent.setStatus(DocumentStatus.MODIFIED);
            //Setting orders as billed = false to free them. Orders staying in the invoice will be setted back to billed = true in CreateDocument
            entityParent.getOrders().forEach(order -> {
                order.setBilled(false);
                orderRepository.save(order);
            });
            documentRepository.save(entityParent);
        }

        dto.setIdDocumentParent(entityParent.getIdDocument());
        Integer idCompany = entityParent.getCompany().getIdCompany();

        return createDocument(idCompany, dto, DocumentType.INV_CUST);
    }

    /**
     * Toggle  status to "Paid" or "Unpaid"
     *
     * @param idDocument the ID of the document to delete
     * @throws DocumentNotFoundException if the document does not exist or is not owned by the current user
     */
    public void togglePaidStatus(Integer idDocument){
        Company owner = companyService.getCurrentCompanyOrThrow();
        Document entity = documentRepository.findByOwnerCompanyAndIdDocument(owner, idDocument)
                .orElseThrow(DocumentNotFoundException::new);
        if(entity.getStatus() == DocumentStatus.PAID){
            entity.setStatus(DocumentStatus.PENDING);
        }
        else if(entity.getStatus() == DocumentStatus.PENDING){
            entity.setStatus(DocumentStatus.PAID);
        }
        documentRepository.save(entity);
    }

    /**
     * Soft deletes a document by setting its status to DELETED.
     *
     * @param id the document ID to soft delete
     * @throws DocumentNotFoundException if the document does not exist or is not owned by the current user
     */
    @Transactional
    @Override
    public void softDeleteDocument(Integer id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Document entity = documentRepository.findByOwnerCompanyAndIdDocument(owner, id)
                .orElseThrow(DocumentNotFoundException::new);
        entity.setStatus(DocumentStatus.DELETED);
        entity.getOrders().forEach(order -> {
            order.setBilled(false);
            orderRepository.save(order);
        });
        documentRepository.save(entity);
    }

    /**
     * Validates that all orders belong to the specified company and are not already billed
     * in another active document, except if linked to the given parent document.
     *
     * @param idCompany the company ID to check orders against
     * @param orders    the list of orders involved in the document
     * @param parent    the parent document, if this is a modification; null otherwise
     * @return the company owning the orders
     * @throws CantIncludeOrderAlreadyBilledException if any order is already billed in a different active document
     * @throws OrderDoNotBelongToCompanyException     if any order does not belong to the given company
     */
    private static Company getCompany(Integer idCompany, List<Order> orders, Document parent) {
        Company orderCompany = orders.get(0).getCompany();

        for (Order order : orders) {
            // Check if order is already billed
            if (order.getBilled() != null && order.getBilled()) {
                // Check if order is part of the parent document (if any)
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
