package org.hanghae.hanghaetask2alarm.domain.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long restockRound;

    @Column(nullable = false)
    private Long stock;

    public void updateStock(Long stock) {
        this.stock += stock;
    }

    public void updateRestockRound() {
        this.restockRound += 1;
    }
}
