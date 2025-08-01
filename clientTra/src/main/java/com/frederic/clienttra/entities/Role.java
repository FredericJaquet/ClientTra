package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a user role in the system.
 */
@Entity
@Table(name = "Roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Integer idRole;

    @Column(nullable = false, length = 20, unique = true)
    private String roleName;
}
