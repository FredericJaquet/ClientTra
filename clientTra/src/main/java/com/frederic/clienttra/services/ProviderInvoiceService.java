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

@Service
@RequiredArgsConstructor
public class ProviderInvoiceService implements DocumentService{

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

    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getAllDocumentsByType(DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<DocumentListProjection> entities = documentRepository.findListByDocTypeAndOwnerCompany(type, owner);

        return documentMapper.toListDtosFromProjection(entities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByCompanyId(DocumentType type, Integer idCompany) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<DocumentListProjection> entities = documentRepository.findListByDocTypeIdCompanyAndOwnerCompany(type, idCompany, owner);

        return documentMapper.toListDtosFromProjection(entities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByStatus(DocumentType type, DocumentStatus status) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<DocumentListProjection> entities = documentRepository.findListByDocTypeStatusAndOwnerCompany(type, status, owner);

        return documentMapper.toListDtosFromProjection(entities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentForListDTO> getDocumentsByIdCompanyAndStatus(DocumentType type, Integer idCompany, DocumentStatus status) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<DocumentListProjection> entities = documentRepository.findListByDocTypeIdCompanyStatusAndOwnerCompany(type, idCompany, status, owner);
        return documentMapper.toListDtosFromProjection(entities);
    }

    @Transactional(readOnly = true)
    @Override
    public DocumentDTO getDocumentById(DocumentType type, Integer id) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Document entity = documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, id, type)
                .orElseThrow(DocumentNotFoundException::new);

        return documentMapper.toDto(entity);
    }

    @Transactional
    @Override
    public DocumentDTO createDocument(Integer idCompany, BaseDocumentDTO dto, DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String currency=null;
        LocalDate deadline=null;

        // 1. Recuperar entidades relacionadas
        ChangeRate changeRate = changeRateService.getChangeRateByIdAndOwner(dto.getIdChangeRate(), owner);
        BankAccount bankAccount = dto.getIdBankAccount() != null ? bankAccountService.getBankAccountByIdAndOwner(dto.getIdBankAccount(), owner) : null;
        Document parent = dto.getIdDocumentParent() == null ? null : documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, dto.getIdDocumentParent(), type)
                .orElseThrow(DocumentNotFoundException::new);
        List<Order> orders = orderRepository.findAllByIdOrderInAndOwnerCompany(dto.getOrderIds(), owner);
        if (orders.isEmpty()) {
            throw new CantCreateDocumentWithoutOrdersException();
        }

        //Verificar que los % son correctos
        if(dto.getVatRate()<1){
            throw new InvalidVatRateException();
        }
        if(dto.getWithholding()<1){
            throw new InvalidWithholdingException();
        }

        Company orderCompany = getCompany(idCompany, orders, parent);

        Company company=companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);

        //2. Campos calculados
        Provider provider=providerRepository.findByOwnerCompanyAndCompany(owner,orderCompany)
                .orElseThrow(ProviderNotFoundException::new);

        currency = changeRate.getCurrency1();
        deadline = documentUtils.calculateDeadline(dto.getDocDate(), provider.getDuedate());


        // 3. Crear entidad
        Document entity = documentMapper.toEntity(dto, changeRate, bankAccount, parent, orders);
        entity.setOwnerCompany(owner);
        entity.setCompany(company);
        entity.setCurrency(currency);
        entity.setDeadline(deadline);
        entity.setDocType(type);

        //4. Calcular los totales
        documentUtils.calculateTotals(entity);

        // 5. Marcar los pedidos como facturados (antes de guardar el documento)
        orderService.markOrdersAsBilled(orders);

        // 6. Guardar el documento
        Document newEntity = documentRepository.save(entity);

        return documentMapper.toDto(newEntity);
    }

    @Transactional
    @Override
    public DocumentDTO updateDocument(Integer idDocument, BaseDocumentDTO dto, DocumentType type) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Document entityParent = documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner,idDocument, type)
                .orElseThrow(DocumentNotFoundException::new);

        if(entityParent.getStatus().equals(DocumentStatus.MODIFIED)){
            throw new CantModifyDocumentAlreadyModified();
        }

        if(entityParent.getDocType().equals(DocumentType.INV_PROV)){
            if(!entityParent.getStatus().equals(DocumentStatus.PENDING)){
                throw new CantModifyPaidInvoiceException();
            }
            entityParent.setStatus(DocumentStatus.MODIFIED);
            documentRepository.save(entityParent);
        }

        dto.setIdDocumentParent(entityParent.getIdDocument());
        Integer idCompany=entityParent.getCompany().getIdCompany();

        return createDocument(idCompany,dto,DocumentType.INV_PROV);
    }

    @Transactional
    @Override
    public void softDeleteDocument(Integer id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Document entity = documentRepository.findByOwnerCompanyAndIdDocument(owner, id)
                .orElseThrow(DocumentNotFoundException::new);
        entity.setStatus(DocumentStatus.DELETED);
        documentRepository.save(entity);
    }

    private static Company getCompany(Integer idCompany, List<Order> orders, Document parent) {
        Company orderCompany= orders.getFirst().getCompany();

        for(Order order : orders){
            // Verifica si el pedido ya está vinculado a otra factura activa
            if (order.getBilled() != null && order.getBilled()) {
                // Busca si el pedido está en la factura "padre"
                boolean isPartOfModifiedInvoice = parent != null && parent.getOrders().contains(order);

                if (!isPartOfModifiedInvoice) {
                    throw new CantIncludeOrderAlreadyBilledException();
                }
            }
            if(!Objects.equals(orderCompany.getIdCompany(), idCompany)){
                throw new OrderDoNotBelongToCompanyException();
            }
        }
        return orderCompany;
    }
}
