package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.exceptions.*;
import com.frederic.clienttra.mappers.DocumentMapper;
import com.frederic.clienttra.repositories.*;
import com.frederic.clienttra.utils.DocumentUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PurchaseOrderServiceTest {

    @Mock
    private DocumentMapper documentMapper;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private ChangeRateService changeRateService;
    @Mock
    private CompanyService companyService;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ProviderRepository providerRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private DocumentUtils documentUtils;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDocument_shouldCreateDocumentCorrectly() {
        int idOwnerCompany = 1;
        int idProviderCompany = 2;
        int idTargetCompany = 2;
        int idChangeRate = 3;
        DocumentType type = DocumentType.PO;

        Company owner = new Company();
        owner.setIdCompany(idOwnerCompany);
        Company providerCompany = new Company();
        providerCompany.setIdCompany(idProviderCompany);
        Company targetCompany = new Company();
        targetCompany.setIdCompany(idTargetCompany);

        ChangeRate changeRate = new ChangeRate();
        changeRate.setCurrency1("EUR");
        changeRate.setIdChangeRate(idChangeRate);

        Provider provider = new Provider();
        provider.setDuedate(15);

        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setDocDate(LocalDate.of(2024, 1, 10));
        dto.setIdChangeRate(idChangeRate);
        dto.setVatRate(21.0);
        dto.setWithholding(15.0);
        dto.setOrderIds(List.of(100));

        Order order = new Order();
        order.setIdOrder(100);
        order.setCompany(providerCompany);
        order.setOwnerCompany(owner);
        order.setBilled(false);

        Document entity = new Document();
        Document saved = new Document();
        DocumentDTO expectedDto = new DocumentDTO();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(changeRateService.getChangeRateByIdAndOwner(idChangeRate, owner)).thenReturn(changeRate);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(dto.getOrderIds(), owner)).thenReturn(List.of(order));
        when(companyRepository.findByIdCompany(idTargetCompany)).thenReturn(Optional.of(targetCompany));
        when(providerRepository.findByOwnerCompanyAndCompany(owner, providerCompany)).thenReturn(Optional.of(provider));
        when(documentUtils.calculateDeadline(any(), eq(15))).thenReturn(LocalDate.of(2024, 1, 25));
        when(documentMapper.toEntity(eq(dto), eq(changeRate), any(), any(), eq(List.of(order)))).thenReturn(entity);
        when(documentRepository.save(entity)).thenReturn(saved);
        when(documentMapper.toDto(saved)).thenReturn(expectedDto);

        DocumentDTO result = purchaseOrderService.createDocument(idProviderCompany, dto, type);

        assertThat(result).isEqualTo(expectedDto);
        verify(documentUtils).calculateTotals(entity);
    }

    @Test
    void createDocument_shouldThrow_WhenNoOrdersFound() {
        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setOrderIds(List.of(1));
        dto.setVatRate(21.0);
        dto.setWithholding(15.0);

        Company owner = new Company();
        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(dto.getOrderIds(), owner)).thenReturn(List.of());

        assertThatThrownBy(() ->
                purchaseOrderService.createDocument(1, dto, DocumentType.PO)
        ).isInstanceOf(CantCreateDocumentWithoutOrdersException.class);

        verifyNoMoreInteractions(documentRepository, documentUtils, documentMapper);
    }

    @Test
    void createDocument_shouldThrow_WhenVatRateIsInvalid() {
        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setOrderIds(List.of(1));
        dto.setVatRate(0.5);
        dto.setWithholding(1.0);

        Company owner = new Company();
        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(any(), any())).thenReturn(List.of(mock(Order.class)));

        assertThatThrownBy(() ->
                purchaseOrderService.createDocument(1, dto, DocumentType.PO)
        ).isInstanceOf(InvalidVatRateException.class);

        verifyNoMoreInteractions(documentRepository, documentUtils, documentMapper);
    }

    @Test
    void createDocument_shouldThrow_WhenWithholdingIsInvalid() {
        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setOrderIds(List.of(1));
        dto.setVatRate(21.0);
        dto.setWithholding(0.5);

        Company owner = new Company();
        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(any(), any())).thenReturn(List.of(mock(Order.class)));

        assertThatThrownBy(() ->
                purchaseOrderService.createDocument(1, dto, DocumentType.PO)
        ).isInstanceOf(InvalidWithholdingException.class);

        verifyNoMoreInteractions(documentRepository, documentUtils, documentMapper);
    }

    @Test
    void createDocument_shouldThrow_WhenOrderFromDifferentCompany() {
        int idOwnerCompany = 1;
        int idRealOrderCompany = 99;// diferente

        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setOrderIds(List.of(1));
        dto.setVatRate(21.0);
        dto.setWithholding(2.0);

        Company owner = new Company();
        owner.setIdCompany(idOwnerCompany);
        Company realOrderCompany = new Company();
        realOrderCompany.setIdCompany(idRealOrderCompany);
        Order order = new Order();
        order.setCompany(realOrderCompany);

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(any(), eq(owner))).thenReturn(List.of(order));

        assertThatThrownBy(() ->
                purchaseOrderService.createDocument(idOwnerCompany, dto, DocumentType.PO)
        ).isInstanceOf(OrderDoNotBelongToCompanyException.class);

        verifyNoMoreInteractions(documentRepository, documentUtils, documentMapper);
    }

    @Test
    void createDocument_shouldThrow_WhenProviderNotFound() {
        int idOwnerCompany = 1;
        int idProviderCompany = 2;

        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setOrderIds(List.of(1));
        dto.setVatRate(21.0);
        dto.setWithholding(2.0);

        Company owner = new Company();
        owner.setIdCompany(idOwnerCompany);
        Company providerCompany = new Company();
        providerCompany.setIdCompany(idProviderCompany);
        Order order = new Order();
        order.setCompany(providerCompany);

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(any(), any())).thenReturn(List.of(order));
        when(companyRepository.findByIdCompany(idProviderCompany)).thenReturn(Optional.of(providerCompany));
        when(providerRepository.findByOwnerCompanyAndCompany(owner, providerCompany)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                purchaseOrderService.createDocument(idProviderCompany, dto, DocumentType.PO)
        ).isInstanceOf(CustomerNotFoundException.class);

        verifyNoMoreInteractions(documentRepository, documentUtils, documentMapper);
    }

    @Test
    void updateDocument_ShouldUpdateSuccessfully_WhenValid() {
        int idDocument = 10;
        int idOwnerCompany = 1;
        int idProviderCompany = 2;
        int idBankAccount = 3;
        int idChangeRate = 5;

        Company owner = new Company();
        owner.setIdCompany(idOwnerCompany);
        Company providerCompany = new Company();
        providerCompany.setIdCompany(idProviderCompany);

        ChangeRate changeRate = new ChangeRate();
        changeRate.setIdChangeRate(idChangeRate);
        changeRate.setCurrency1("€");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setIdBankAccount(idBankAccount);

        Provider provider = new Provider();
        provider.setDuedate(30);

        Order order = new Order();
        order.setIdOrder(100);
        order.setCompany(providerCompany);
        order.setOwnerCompany(owner);
        order.setBilled(false);

        Document parent = new Document();
        parent.setIdDocument(idDocument);
        parent.setCompany(providerCompany);
        parent.setStatus(DocumentStatus.PENDING);
        parent.setDocType(DocumentType.PO);
        parent.setOrders(List.of(order));
        parent.setChangeRate(changeRate);

        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setIdChangeRate(idChangeRate);
        dto.setIdBankAccount(idBankAccount);
        dto.setOrderIds(List.of(100));
        dto.setDocDate(LocalDate.of(2025, 7,26));
        dto.setVatRate(21.0);
        dto.setWithholding(15.0);

        DocumentDTO expected = new DocumentDTO();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, idDocument, DocumentType.PO))
                .thenReturn(Optional.of(parent));
        when(providerRepository.findByOwnerCompanyAndCompany(owner, providerCompany)).thenReturn(Optional.of(provider));
        when(changeRateService.getChangeRateByIdAndOwner(idChangeRate, owner)).thenReturn(changeRate);
        when(bankAccountService.getBankAccountByIdAndOwner(idBankAccount, owner)).thenReturn(bankAccount);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(dto.getOrderIds(),owner)).thenReturn(List.of(order));
        when(documentUtils.calculateDeadline(any(), eq(15))).thenReturn(LocalDate.of(2025, 8, 25));
        when(documentRepository.save(parent)).thenReturn(parent);
        when(documentMapper.toDto(parent)).thenReturn(expected);

        DocumentDTO result = purchaseOrderService.updateDocument(idDocument, dto, DocumentType.PO);

        assertThat(result).isEqualTo(expected);

        verify(documentMapper).updateEntity(eq(parent), eq(dto), eq(changeRate), eq(bankAccount), isNull(), eq(List.of(order)));
        verify(documentRepository).save(parent);
    }

    @Test
    void updateDocument_ShouldThrowDocumentNotFoundException_WhenNotFound() {
        int idDocument = 1;
        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(new Company());
        when(documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(any(), eq(idDocument), eq(DocumentType.PO)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> purchaseOrderService.updateDocument(idDocument, dto, DocumentType.PO))
                .isInstanceOf(DocumentNotFoundException.class);
    }

    @Test
    void softDelete_ShouldMarkDocumentAsDELETED_IfFound(){
        int idDoc = 1;
        Document doc = new Document();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(new Company());
        when(documentRepository.findByOwnerCompanyAndIdDocument(any(), anyInt()))
                .thenReturn(Optional.of(doc));

        purchaseOrderService.softDeleteDocument(idDoc);

        assertThat(doc.getStatus()).isEqualTo(DocumentStatus.DELETED);

        verify(documentRepository).save(doc);
    }

    @Test
    void updateDocument_shouldThrow_WhenVatRateIsInvalid() {
        int idDocument = 10;
        int idOwnerCompany = 1;
        int idProviderCompany = 2;
        int idChangeRate = 3;

        Company owner = new Company();
        owner.setIdCompany(idOwnerCompany);

        Company providerCompany = new Company();
        providerCompany.setIdCompany(idProviderCompany);

        ChangeRate changeRate = new ChangeRate();
        changeRate.setIdChangeRate(idChangeRate);
        changeRate.setCurrency1("€");

        Document document = new Document();
        document.setIdDocument(idDocument);
        document.setCompany(providerCompany);
        document.setDocType(DocumentType.PO);
        document.setChangeRate(changeRate);

        Provider provider = new Provider();
        provider.setDuedate(30);

        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setVatRate(0.5);  // Invalid
        dto.setWithholding(10.0);

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, idDocument, DocumentType.PO))
                .thenReturn(Optional.of(document));
        when(providerRepository.findByOwnerCompanyAndCompany(owner, providerCompany)).thenReturn(Optional.of(provider));

        assertThatThrownBy(() ->
                purchaseOrderService.updateDocument(idDocument, dto, DocumentType.PO)
        ).isInstanceOf(InvalidVatRateException.class);
    }

    @Test
    void updateDocument_shouldThrow_WhenWithholdingIsInvalid() {
        int idDocument = 10;
        int idOwnerCompany = 1;
        int idProviderCompany = 2;
        int idChangeRate = 3;

        Company owner = new Company();
        owner.setIdCompany(idOwnerCompany);

        Company providerCompany = new Company();
        providerCompany.setIdCompany(idProviderCompany);

        ChangeRate changeRate = new ChangeRate();
        changeRate.setIdChangeRate(idChangeRate);
        changeRate.setCurrency1("€");

        Document document = new Document();
        document.setIdDocument(idDocument);
        document.setCompany(providerCompany);
        document.setDocType(DocumentType.PO);
        document.setChangeRate(changeRate);

        Provider provider = new Provider();
        provider.setDuedate(30);

        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setVatRate(21.0);
        dto.setWithholding(0.5);// Invalid

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, idDocument, DocumentType.PO))
                .thenReturn(Optional.of(document));
        when(providerRepository.findByOwnerCompanyAndCompany(owner, providerCompany)).thenReturn(Optional.of(provider));

        assertThatThrownBy(() ->
                purchaseOrderService.updateDocument(idDocument, dto, DocumentType.PO)
        ).isInstanceOf(InvalidWithholdingException.class);
    }

}
