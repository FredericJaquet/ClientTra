package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrders;

    @Column(nullable = false, length = 100)
    private String descrip;

    @Column(nullable = false)
    private LocalDate dateOrder;

    @Column(nullable = false)
    private Double pricePerUnit;

    @Column(length = 15)
    private String units;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private Boolean billed;

    @Column(length = 25)
    private String fieldName;

    @Column(length = 15)
    private String sourceLanguage;

    @Column(length = 15)
    private String targetLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCompany", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOwnerCompany", nullable = false)
    private Company ownerCompany;

    @ManyToMany(mappedBy = "orders")
    private Set<Document> documents = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items = new HashSet<>();

}
