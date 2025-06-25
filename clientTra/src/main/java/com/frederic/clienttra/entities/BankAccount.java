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
    @Column(name = "id_bank_account")
    private Integer idBankAccount;

    @Column(nullable = false, length = 34, unique = true)
    private String iban;

    @Column(length = 15)
    private String swift;

    @Column(length = 100)
    private String branch;

    @Column(length = 100)
    private String holder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", nullable = false)
    private Company company;
}

