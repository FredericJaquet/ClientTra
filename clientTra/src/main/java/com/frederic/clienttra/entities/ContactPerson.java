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

    @Column(nullable = false, length = 40)
    private String firstname;

    @Column(nullable = false, length = 40)
    private String middlename;

    @Column(length = 40)
    private String lastname;

    @Column(length = 40)
    private String role;

    @Column(length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", nullable = false)
    private Company company;
}
