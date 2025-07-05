package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_provider")
    private Integer idProvider;
    @Column(length = 10)
    private String defaultLanguage;
    @Column
    private Double defaultVat;
    @Column
    private Double defaultWithholding;
    @Column
    private Integer duedate;
    @Column
    private Boolean europe;
    @Column
    private Boolean enabled;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", nullable = false)
    private Company company;
    // This relationship points to the entity that owns the provider.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_owner_company", nullable = false)
    private Company ownerCompany;
}
