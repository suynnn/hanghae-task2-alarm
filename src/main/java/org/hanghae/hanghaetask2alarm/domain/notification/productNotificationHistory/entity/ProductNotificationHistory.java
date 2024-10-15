package org.hanghae.hanghaetask2alarm.domain.notification.productNotificationHistory.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanghae.hanghaetask2alarm.domain.product.entity.Product;

@Entity
@Table
@Getter
@NoArgsConstructor
public class ProductNotificationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductNotificationStatus productNotificationStatus;

    @Column(nullable = false)
    private Long lastSentUserId;

    @Builder
    public ProductNotificationHistory (Product product, ProductNotificationStatus productNotificationStatus, Long lastSentUserId) {
        this.product = product;
        this.productNotificationStatus = productNotificationStatus;
        this.lastSentUserId = lastSentUserId;
    }
}
