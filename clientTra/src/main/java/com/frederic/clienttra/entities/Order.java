package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an order made by a company, including details like description,
 * date, pricing, languages involved, billing status, related items, and
 * associated documents.
 */
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
    @Column(name = "id_order")
    private Integer idOrder;
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
    @JoinColumn(name = "id_company", nullable = false)
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_owner_company", nullable = false)
    private Company ownerCompany;
    @ManyToMany(mappedBy = "orders")
    private List<Document> documents = new ArrayList<>();
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();
}
