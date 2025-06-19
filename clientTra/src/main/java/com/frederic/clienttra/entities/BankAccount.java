package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BankAccounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBankAccount;

    @Column(nullable = false, length = 34, unique = true)
    private String iban;

    @Column(length = 15)
    private String bic;

    @Column(length = 100)
    private String bankName;

    @Column(length = 100)
    private String holderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCompany", nullable = false)
    private Company company;
}

