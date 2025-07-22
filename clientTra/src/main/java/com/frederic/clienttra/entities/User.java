package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;
    @Column(nullable = false, unique = true, length = 50)
    private String userName;
    @Column(nullable = false, length = 255)
    private String passwd;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(length = 10)
    private String preferredLanguage;
    @Column(length = 20)
    private String preferredTheme;
    @Column
    private boolean darkMode;
    @Column(nullable = false)
    private boolean enabled = true;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_plan", nullable = false)
    private Plan plan;
}
