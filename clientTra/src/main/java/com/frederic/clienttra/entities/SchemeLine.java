package com.frederic.clienttra.entities;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a line within a pricing scheme, including a description and an optional discount.
 */
@Entity
@Table(name = "SchemeLines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_scheme_line")
    private Integer idSchemeLine;
    @Column(length = 255)
    private String descrip;
    @Column
    private Double discount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_scheme", nullable = false)
    private Scheme scheme;
}
