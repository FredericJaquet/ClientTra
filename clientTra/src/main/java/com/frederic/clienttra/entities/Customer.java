package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private Integer idCustomer;
    @Column(length = 50)
    private String invoicingMethod;
    @Column
    private Integer duedate;
    @Column(length = 50)
    private String payMethod;
    @Column(length = 10)
    private String defaultLanguage;
    @Column
    private Double defaultVat;
    @Column
    private Double defaultWithholding;
    @Column
    private Boolean europe;
    @Column
    private Boolean enabled;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", nullable = false)
    private Company company;
    // This relationship points to the entity that owns the customer.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_owner_company", nullable = false)
    private Company ownerCompany;
}
