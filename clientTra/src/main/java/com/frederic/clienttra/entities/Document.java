package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private Integer idDocument;

    @Column(nullable = false, length = 50)
    private String docNumber;

    @Column(nullable = false)
    private LocalDate docDate;

    @Column(nullable = false, length = 20)
    private String docType;

    @Column(nullable = false, length = 20)
    private String status;

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
    @JoinColumn(name = "idCompany", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idChangeRate")
    private ChangeRate changeRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idBankAccount")
    private BankAccount bankAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDocumentParent")
    private Document documentParent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOwnerCompany", nullable = false)
    private Company ownerCompany;

    @ManyToMany
    @JoinTable(
            name = "DocumentOrders",
            joinColumns = @JoinColumn(name = "idDocument"),
            inverseJoinColumns = @JoinColumn(name = "idOrders")
    )
    private Set<Order> orders = new HashSet<>();
}
