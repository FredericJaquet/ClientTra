package com.frederic.clienttra.entities;

import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document")
    private Integer idDocument;
    @Column(nullable = false, length = 50)
    private String docNumber;
    @Column(nullable = false)
    private LocalDate docDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DocumentType docType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DocumentStatus status;
    @Column(length = 10)
    private String language;
    @Column(nullable = false)
    private Double vatRate;
    @Column(nullable = false)
    private Double withholding;
    @Column(nullable = false)
    private Double totalNet;
    @Column(nullable = false)
    private Double totalVat;
    @Column(nullable = false)
    private Double totalGross;
    @Column(nullable = false)
    private Double totalWithholding;
    @Column(nullable = false)
    private Double totalToPay;
    @Column(length = 10)
    private String currency;
    @Column(length = 255)
    private String noteDelivery;
    @Column(length = 255)
    private String notePayment;
    @Column
    private LocalDate deadline;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", nullable = false)
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_change_rate")
    private ChangeRate changeRate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bank_account")
    private BankAccount bankAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_document_parent")
    private Document documentParent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_owner_company", nullable = false)
    private Company ownerCompany;
    @ManyToMany
    @JoinTable(
            name = "document_orders",
            joinColumns = @JoinColumn(name = "id_document"),
            inverseJoinColumns = @JoinColumn(name = "id_order")
    )
    private List<Order> orders = new ArrayList<>();
}
