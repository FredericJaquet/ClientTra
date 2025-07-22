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
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactPerson> contactPersons=new ArrayList<>();
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccount> bankAccounts=new ArrayList<>();
    @OneToMany(mappedBy = "ownerCompany")
    private List<ChangeRate> changeRates=new ArrayList<>();
    @OneToMany(mappedBy = "company")
    private List<User> users=new ArrayList<>();
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Customer> customers=new ArrayList<>();
    @OneToMany(mappedBy = "ownerCompany")
    private List<Customer> ownedCustomers=new ArrayList<>();
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Provider> providers=new ArrayList<>();
    @OneToMany(mappedBy = "ownerCompany")
    private List<Provider> ownedProviders=new ArrayList<>();
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scheme> schemes=new ArrayList<>();
    @OneToMany(mappedBy = "ownerCompany")
    private List<Scheme> ownedSchemes=new ArrayList<>();
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents=new ArrayList<>();
    @OneToMany(mappedBy = "ownerCompany")
    private List<Document> ownedDocuments=new ArrayList<>();
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders=new ArrayList<>();
    @OneToMany(mappedBy = "ownerCompany")
    private List<Order> ownedOrders=new ArrayList<>();

    public void addAddress(Address address) {
        if (addresses == null) {
            addresses = new ArrayList<>();
        }
        addresses.add(address);
        address.setCompany(this);
    }

    public void addPhone(Phone phone) {
        if (phones == null) {
            phones = new ArrayList<>();
        }
        phones.add(phone);
        phone.setCompany(this);
    }

    public void addBankAccount(BankAccount bankAccount) {
        if (bankAccounts == null) {
            bankAccounts = new ArrayList<>();
        }
        bankAccounts.add(bankAccount);
        bankAccount.setCompany(this);
    }

    public void addContactPerson(ContactPerson contact){
        if(contactPersons == null){
            contactPersons = new ArrayList<>();
        }
        contactPersons.add(contact);
        contact.setCompany(this);
    }

    public void addDocument(Document document){
        if(documents == null){
            documents = new ArrayList<>();
        }
        documents.add(document);
        document.setCompany(this);
    }

    public void addOrder(Order order){
        if(orders == null){
            orders = new ArrayList<>();
        }
        orders.add(order);
        order.setCompany(this);
    }

    public void addScheme(Scheme scheme){
        if(schemes == null) {
            schemes = new ArrayList<>();
        }
        schemes.add(scheme);
        scheme.setCompany(this);
    }

    public void addChangeRate(ChangeRate changeRate){
        if(changeRates == null){
            changeRates = new ArrayList<>();
        }
        changeRates.add(changeRate);
        changeRate.setOwnerCompany(this);
    }

    public void addUser(User user){
        if(users == null){
            users = new ArrayList<>();
        }
        users.add(user);
        user.setCompany(this);
    }


}

