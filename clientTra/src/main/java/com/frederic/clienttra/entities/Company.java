package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Companies",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"vat_number", "id_owner_company"}),
        @UniqueConstraint(columnNames = {"legal_name", "id_owner_company"})
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_company")
    private Integer idCompany;

    @Column(length = 25, nullable = false)
    private String vatNumber;

    @Column(length = 100)
    private String comName;

    @Column(length = 100, nullable = false, unique = true)
    private String legalName;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String web;

    @Column(length = 255)
    private String logoPath;

    // Auto-relación (entidad propietaria)
    @ManyToOne
    @JoinColumn(name = "id_owner_company", referencedColumnName = "id_company")
    private Company ownerCompany;

    @OneToMany(mappedBy = "ownerCompany")
    private List<Company> childCompanies=new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses=new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones=new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<ContactPerson> contactPersons=new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccount> bankAccounts=new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<User> users=new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<Customer> customers=new ArrayList<>();

    @OneToMany(mappedBy = "ownerCompany")
    private List<Customer> ownedCustomers=new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<Provider> providers=new ArrayList<>();

    @OneToMany(mappedBy = "ownerCompany")
    private List<Provider> ownedProviders=new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<Scheme> schemes=new ArrayList<>();

    @OneToMany(mappedBy = "ownerCompany")
    private List<Scheme> ownedSchemes=new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<Document> documents=new ArrayList<>();

    @OneToMany(mappedBy = "ownerCompany")
    private List<Document> ownedDocuments=new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<Order> orders=new ArrayList<>();

    @OneToMany(mappedBy = "ownerCompany")
    private List<Order> ownedOrders=new ArrayList<>();
}

