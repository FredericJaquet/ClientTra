package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a subscription plan available in the system.
 */
@Entity
@Table(name = "Plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Integer idPlan;

    @Column(nullable = false, unique = true, length = 100)
    private String planName;
}
