package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Phones",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"phone_number", "id_owner_entity"})
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phone")
    private int idPhone;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(length = 20)
    private String phoneType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", nullable = false)
    private Company company;

}
