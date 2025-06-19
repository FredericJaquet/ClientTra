package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChangeRates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idChangeRate;

    @Column(length = 10, nullable = false)
    private String currency1;

    @Column(length = 10, nullable = false)
    private String currency2;

    @Column(nullable = false)
    private Double rate;
}
