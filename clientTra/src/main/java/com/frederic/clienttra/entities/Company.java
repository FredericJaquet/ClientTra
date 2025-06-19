package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCompany;

    @Column(length = 25, nullable = false, unique = true)
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
    @JoinColumn(name = "idOwnerCompany", referencedColumnName = "idCompany")
    private Company ownerCompany;

    // Relaciones inversas
    @OneToMany(mappedBy = "ownerCompany")
    private List<Company> childCompanies;//Usar Set en vez de List?

    @OneToMany(mappedBy = "company")
    private List<Address> addresses;

    @OneToMany(mappedBy = "company")
    private List<Phone> phones;

    @OneToMany(mappedBy = "company")
    private List<ContactPerson> contactPersons;

    @OneToMany(mappedBy = "company")
    private List<BankAccount> bankAccounts;

    @OneToMany(mappedBy = "company")
    private List<User> users;

    @OneToMany(mappedBy = "company")
    private List<Customer> customers;

    @OneToMany(mappedBy = "ownerCompany")
    private List<Customer> ownedCustomers;

    @OneToMany(mappedBy = "company")
    private List<Provider> providers;

    @OneToMany(mappedBy = "ownerCompany")
    private List<Provider> ownedProviders;

    @OneToMany(mappedBy = "company")
    private List<Scheme> schemes;

    @OneToMany(mappedBy = "ownerCompany")
    private List<Scheme> ownedSchemes;

    @OneToMany(mappedBy = "company")
    private List<Document> documents;

    @OneToMany(mappedBy = "ownerCompany")
    private List<Document> ownedDocuments;

    @OneToMany(mappedBy = "company")
    private List<Order> orders;

    @OneToMany(mappedBy = "ownerCompany")
    private List<Order> ownedOrders;
}

