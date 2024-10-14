package org.hanghae.hanghaetask2alarm.domain.productUserNotificationHistory.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanghae.hanghaetask2alarm.domain.product.entity.Product;
import org.hanghae.hanghaetask2alarm.domain.user.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductUserNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long restockRound;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Builder
    public ProductUserNotificationHistory(Product product, User user, Long restockRound) {
        this.product = product;
        this.user = user;
        this.restockRound = restockRound;
    }
}
