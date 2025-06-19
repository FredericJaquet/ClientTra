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
    private Double defaultVAT;

    @Column
    private Double defaultWithholding;

    @Column
    private Boolean europe;

    @Column
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCompany", nullable = false)
    private Company company;

    // This relationship points to the entity that owns the customer.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOwnerCompany", nullable = false)
    private Company ownerCompany;
}
