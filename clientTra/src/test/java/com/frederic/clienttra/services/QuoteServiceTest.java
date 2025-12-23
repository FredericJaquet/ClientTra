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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class QuoteServiceTest {

    @Mock
    private CompanyService companyService;
    @Mock
    private ChangeRateService changeRateService;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private DocumentUtils documentUtils;
    @Mock
    private DocumentMapper documentMapper;

    @InjectMocks
    private QuoteService quoteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateDocument_ShouldUpdateSuccessfully_WhenValid() {
        int idDocument = 10;
        int idOwnerCompany = 1;
        int idCustomerCompany = 2;
        int idBankAccount = 3;
        int idChangeRate = 5;

        Company owner = new Company();
        owner.setIdCompany(idOwnerCompany);
        Company customerCompany = new Company();
        customerCompany.setIdCompany(idCustomerCompany);

        ChangeRate changeRate = new ChangeRate();
        changeRate.setIdChangeRate(idChangeRate);
        changeRate.setCurrency1("€");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setIdBankAccount(idBankAccount);

        Customer customer = new Customer();
        customer.setDuedate(30);

        Order order = new Order();
        order.setIdOrder(100);
        order.setCompany(customerCompany);
        order.setOwnerCompany(owner);
        order.setBilled(false);

        Document parent = new Document();
        parent.setIdDocument(idDocument);
        parent.setCompany(customerCompany);
        parent.setStatus(DocumentStatus.PENDING);
        parent.setDocType(DocumentType.QUOTE);
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
        when(documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(owner, idDocument, DocumentType.QUOTE))
                .thenReturn(Optional.of(parent));
        when(customerRepository.findByOwnerCompanyAndCompany(owner, customerCompany)).thenReturn(Optional.of(customer));
        when(changeRateService.getChangeRateByIdAndOwner(idChangeRate, owner)).thenReturn(changeRate);
        when(bankAccountService.getBankAccountByIdAndOwner(idBankAccount, owner)).thenReturn(bankAccount);
        when(orderRepository.findAllByIdOrderInAndOwnerCompany(dto.getOrderIds(),owner)).thenReturn(List.of(order));
        when(documentUtils.generateNotePayment(any(), eq(customer), any())).thenReturn("Pago en 30 días");
        when(documentUtils.calculateDeadline(any(), eq(15))).thenReturn(LocalDate.of(2025, 8, 25));
        when(documentRepository.save(parent)).thenReturn(parent);
        when(documentMapper.toDto(parent)).thenReturn(expected);

        DocumentDTO result = quoteService.updateDocument(idDocument, dto, DocumentType.QUOTE);

        assertThat(result).isEqualTo(expected);

        verify(documentMapper).updateEntity(eq(parent), eq(dto), eq(changeRate), eq(bankAccount), isNull(), eq(List.of(order)));
        verify(documentRepository).save(parent);
    }

    @Test
    void updateDocument_ShouldThrowDocumentNotFoundException_WhenNotFound() {
        int idDocument = 1;
        CreateDocumentRequestDTO dto = new CreateDocumentRequestDTO();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(new Company());
        when(documentRepository.findByOwnerCompanyAndIdDocumentAndDocType(any(), eq(idDocument), eq(DocumentType.QUOTE)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> quoteService.updateDocument(idDocument, dto, DocumentType.QUOTE))
                .isInstanceOf(DocumentNotFoundException.class);
    }

    @Test
    void softDelete_ShouldMarkDocumentAsDELETED_IfFound(){
        int idDoc = 1;
        Document doc = new Document();

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(new Company());
        when(documentRepository.findByOwnerCompanyAndIdDocument(any(), anyInt()))
                .thenReturn(Optional.of(doc));

        quoteService.softDeleteDocument(idDoc);

        assertThat(doc.getStatus()).isEqualTo(DocumentStatus.DELETED);

        verify(documentRepository).save(doc);
    }

}
