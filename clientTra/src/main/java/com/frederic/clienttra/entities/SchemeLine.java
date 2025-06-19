package com.frederic.clienttra.entities;
import jakarta.persistence.*;
import lombok.*;

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
    private Integer idSchemeLine;

    @Column(length = 255)
    private String descrip;

    @Column
    private Double discount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idScheme", nullable = false)
    private Scheme scheme;
}
