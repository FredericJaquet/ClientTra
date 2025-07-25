package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.exceptions.*;
import com.frederic.clienttra.mappers.DocumentMapper;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.repositories.CustomerRepository;
import com.frederic.clienttra.repositories.DocumentRepository;
import com.frederic.clienttra.repositories.OrderRepository;
import com.frederic.clienttra.utils.DocumentUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CustomerInvoiceServiceTest {

    @Mock
    private DocumentMapper documentMapper;
    @Mock private DocumentRepository documentRepository;
    @Mock private ChangeRateService changeRateService;
    @Mock private CompanyService companyService;
    @Mock private OrderService orderService;
    @Mock private CompanyRepository companyRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private DocumentUtils documentUtils;

    @Spy
    @InjectMocks
    private CustomerInvoiceService customerInvoiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldCreateDocumentCorrectly() {
        int idOwnerCompany = 1;
        int idCustomerCompany = 2;
        int idTargetCompany = 2;
        // Given
        Company owner = new Company();
        owner.setIdCompany(idOwnerCompany);
        Company customerCompany = new Company();
        customerCompany.setIdCompany(idCustomerCompany);
        Company targetCompany = new Company();
        targetCompany.setIdCompany(idTargetCompany);

        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setDocDate(LocalDate.of(2024, 1, 10));
        dto.setIdChangeRate(1);
        dto.setVatRate(21.0);
        dto.setWithholding(15.0);
        dto.setOrderIds(List.of(100));

        ChangeRate changeRate = new ChangeRate();
        changeRate.setCurrency1("EUR");
        Customer customer = new Customer();
        customer.setDuedate(15);

        Order order = new Order();
        order.setIdOrder(100);
        order.setCompany(customerCompany);
        order.setOwnerCompany(owner);
        order.setBilled(null);

        Document entity = new Document();
        Document saved = new Document();
        DocumentDTO expectedDto = new DocumentDTO();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(changeRateService.getChangeRateByIdAndOwner(1, owner)).thenReturn(changeRate);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(List.of(100), owner)).thenReturn(List.of(order));
        when(companyRepository.findByIdCompany(idTargetCompany)).thenReturn(Optional.of(targetCompany));
        when(customerRepository.findByOwnerCompanyAndCompany(owner, customerCompany)).thenReturn(Optional.of(customer));
        when(documentUtils.generateNotePayment(any(), eq(customer), any())).thenReturn("Pago en 15 días");
        when(documentUtils.calculateDeadline(any(), eq(15))).thenReturn(LocalDate.of(2024, 1, 25));
        when(documentMapper.toEntity(eq(dto), eq(changeRate), any(), any(), eq(List.of(order)))).thenReturn(entity);
        when(documentRepository.save(entity)).thenReturn(saved);
        when(documentMapper.toDto(saved)).thenReturn(expectedDto);

        // When
        DocumentDTO result = customerInvoiceService.createDocument(2, dto, DocumentType.INV_CUST);

        // Then
        assertThat(result).isEqualTo(expectedDto);

        verify(orderService).markOrdersAsBilled(List.of(order));
        verify(documentUtils).calculateTotals(entity);
    }

    @Test
    void create_shouldThrow_WhenNoOrdersFound() {
        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setOrderIds(List.of(1));
        dto.setVatRate(21.0);
        dto.setWithholding(15.0);

        Company owner = new Company();
        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(dto.getOrderIds(), owner)).thenReturn(List.of());

        assertThatThrownBy(() ->
                customerInvoiceService.createDocument(1, dto, DocumentType.INV_CUST)
        ).isInstanceOf(CantCreateDocumentWithoutOrdersException.class);

        verifyNoMoreInteractions(documentRepository, documentUtils, documentMapper, orderService);
    }

    @Test
    void create_shouldThrow_WhenVatRateIsInvalid() {
        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setOrderIds(List.of(1));
        dto.setVatRate(0.5);
        dto.setWithholding(1.0);

        Company owner = new Company();
        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(any(), any())).thenReturn(List.of(mock(Order.class)));

        assertThatThrownBy(() ->
                customerInvoiceService.createDocument(1, dto, DocumentType.INV_CUST)
        ).isInstanceOf(InvalidVatRateException.class);

        verifyNoMoreInteractions(documentRepository, documentUtils, documentMapper, orderService);
    }

    @Test
    void create_shouldThrow_WhenWithholdingIsInvalid() {
        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setOrderIds(List.of(1));
        dto.setVatRate(21.0);
        dto.setWithholding(0.5);

        Company owner = new Company();
        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(any(), any())).thenReturn(List.of(mock(Order.class)));

        assertThatThrownBy(() ->
                customerInvoiceService.createDocument(1, dto, DocumentType.INV_CUST)
        ).isInstanceOf(InvalidWithholdingException.class);

        verifyNoMoreInteractions(documentRepository, documentUtils, documentMapper, orderService);
    }

    @Test
    void create_shouldThrow_WhenOrderAlreadyBilled() {
        int idCompany = 1;
        int idOrderCompany = 2;

        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setOrderIds(List.of(1));
        dto.setVatRate(21.0);
        dto.setWithholding(2.0);

        Company owner = new Company();
        owner.setIdCompany(idCompany);
        Company orderCompany = new Company();
        orderCompany.setIdCompany(idOrderCompany);

        Order billedOrder = new Order();
        billedOrder.setBilled(true);
        billedOrder.setCompany(orderCompany);

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(any(), eq(owner))).thenReturn(List.of(billedOrder));

        assertThatThrownBy(() ->
                customerInvoiceService.createDocument(idOrderCompany, dto, DocumentType.INV_CUST)
        ).isInstanceOf(CantIncludeOrderAlreadyBilledException.class);

        verifyNoMoreInteractions(documentRepository, documentUtils, documentMapper, orderService);
    }

    @Test
    void create_shouldThrow_WhenOrderFromDifferentCompany() {
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
                customerInvoiceService.createDocument(idOwnerCompany, dto, DocumentType.INV_CUST)
        ).isInstanceOf(OrderDoNotBelongToCompanyException.class);

        verifyNoMoreInteractions(documentRepository, documentUtils, documentMapper, orderService);
    }

    @Test
    void create_shouldThrow_WhenCustomerNotFound() {
        int idOwnerCompany = 1;
        int idCustomerCompany = 2;

        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setOrderIds(List.of(1));
        dto.setVatRate(21.0);
        dto.setWithholding(2.0);

        Company owner = new Company();
        owner.setIdCompany(idOwnerCompany);
        Company customerCompany = new Company();
        customerCompany.setIdCompany(idCustomerCompany);
        Order order = new Order();
        order.setCompany(customerCompany);

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(any(), any())).thenReturn(List.of(order));
        when(companyRepository.findByIdCompany(idCustomerCompany)).thenReturn(Optional.of(customerCompany));
        when(customerRepository.findByOwnerCompanyAndCompany(owner, customerCompany)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                customerInvoiceService.createDocument(idCustomerCompany, dto, DocumentType.INV_CUST)
        ).isInstanceOf(CustomerNotFoundException.class);

        verifyNoMoreInteractions(documentRepository, documentUtils, documentMapper, orderService);
    }

    @Test
    void updateDocument_ShouldUpdateSuccessfully_WhenValid() {
        int idDocument = 10;
        int idOwnerCompany = 1;
        int idCustomerCompany = 2;

        Company owner = new Company();
        owner.setIdCompany(idOwnerCompany);
        Company customerCompany = new Company();
        customerCompany.setIdCompany(idCustomerCompany);

        Document parent = new Document();
        parent.setIdDocument(idDocument);
        parent.setStatus(DocumentStatus.PENDING);
        parent.setDocType(DocumentType.INV_CUST);
        parent.setCompany(customerCompany);

        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();
        dto.setVatRate(10.0);
        dto.setWithholding(10.0);
        dto.setOrderIds(List.of(1));

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, idDocument, DocumentType.INV_CUST))
                .thenReturn(Optional.of(parent));
        doReturn(new DocumentDTO())
                .when(customerInvoiceService)
                .createDocument(eq(idCustomerCompany), eq(dto), eq(DocumentType.INV_CUST));//Este método permite llamar al Spy,

        DocumentDTO result = customerInvoiceService.updateDocument(idDocument, dto, DocumentType.INV_CUST);

        assertThat(dto.getIdDocumentParent()).isEqualTo(parent.getIdDocument());
        assertThat(parent.getStatus()).isEqualTo(DocumentStatus.MODIFIED);

        verify(documentRepository).save(parent);
        verify(customerInvoiceService).createDocument(idCustomerCompany, dto, DocumentType.INV_CUST);
    }

    @Test
    void updateDocument_ShouldThrowDocumentNotFoundException_WhenNotFound() {
        int idDocument = 1;
        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(new Company());
        when(documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(any(), eq(idDocument), eq(DocumentType.INV_CUST)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerInvoiceService.updateDocument(idDocument, dto, DocumentType.INV_CUST))
                .isInstanceOf(DocumentNotFoundException.class);
    }

    @Test
    void updateDocument_ShouldThrowCantModifyDocumentAlreadyModified_WhenAlreadyModified() {
        Document modified = new Document();
        modified.setStatus(DocumentStatus.MODIFIED);
        modified.setDocType(DocumentType.INV_CUST);

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(new Company());
        when(documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(any(), anyInt(), any()))
                .thenReturn(Optional.of(modified));

        assertThatThrownBy(() -> customerInvoiceService.updateDocument(1, new CreateDocumentRequestDTO(), DocumentType.INV_CUST))
                .isInstanceOf(CantModifyDocumentAlreadyModified.class);
    }

    @Test
    void updateDocument_ShouldThrowCantModifyPaidInvoiceException_WhenInvoiceIsNotPending() {
        Document parent = new Document();
        parent.setStatus(DocumentStatus.PAID);
        parent.setDocType(DocumentType.INV_CUST);

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(new Company());
        when(documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(any(), anyInt(), any()))
                .thenReturn(Optional.of(parent));

        assertThatThrownBy(() -> customerInvoiceService.updateDocument(1, new CreateDocumentRequestDTO(), DocumentType.INV_CUST))
                .isInstanceOf(CantModifyPaidInvoiceException.class);
    }

    @Test
    void softDelete_ShouldMarkDocumentAsDELETED_IfFound(){
        int idDoc = 1;
        Document doc = new Document();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(new Company());
        when(documentRepository.findByOwnerCompanyAndIdDocument(any(), anyInt()))
                .thenReturn(Optional.of(doc));

        customerInvoiceService.softDeleteDocument(idDoc);

        assertThat(doc.getStatus()).isEqualTo(DocumentStatus.DELETED);

        verify(documentRepository).save(doc);
    }










}
