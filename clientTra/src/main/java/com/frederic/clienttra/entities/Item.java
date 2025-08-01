package com.frederic.clienttra.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a line item within an order, including description, quantity,
 * discount, total amount, and its associated order.
 */
@Entity
@Table(name = "Items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Integer idItem;
    @Column(length = 255)
    private String descrip;
    @Column(nullable = false)
    private Double qty;
    @Column
    private Double discount;
    @Column(nullable = false)
    private Double total;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;
}
