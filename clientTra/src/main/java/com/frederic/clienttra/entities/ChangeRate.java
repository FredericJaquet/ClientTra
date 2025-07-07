package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ChangeRates", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"currency1", "currency2", "date", "id_owner_company"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_change_rate")
    private Integer idChangeRate;

    @Column(length = 10, nullable = false)
    private String currency1;

    @Column(length = 10, nullable = false)
    private String currency2;

    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_owner_company")
    private Company ownerCompany;
}

