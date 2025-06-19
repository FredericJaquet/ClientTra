package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAddress;

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 10)
    private String stNumber;

    @Column(length = 100)
    private String apt;

    @Column(nullable = false, length = 10)
    private String cp;

    @Column(nullable = false, length = 40)
    private String city;

    @Column(length = 40)
    private String state;

    @Column(nullable = false, length = 40)
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCompany", nullable = false)
    private Company company;
}
