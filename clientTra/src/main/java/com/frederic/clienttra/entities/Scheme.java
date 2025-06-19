package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "Schemes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idScheme;

    @Column(nullable = false, length = 50)
    private String schemeName;

    @Column(nullable = false)
    private Double price;

    @Column(length = 25)
    private String units;

    @Column(length = 15)
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

    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SchemeLine> schemeLines;
}

