package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ContactPersons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contact_person")
    private Integer idContactPerson;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String middlename;

    @Column(length = 50)
    private String surname;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", nullable = false)
    private Company company;
}
