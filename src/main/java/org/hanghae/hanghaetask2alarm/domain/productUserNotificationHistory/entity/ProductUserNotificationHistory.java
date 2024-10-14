package org.hanghae.hanghaetask2alarm.domain.productUserNotificationHistory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanghae.hanghaetask2alarm.domain.product.entity.Product;
import org.hanghae.hanghaetask2alarm.domain.user.entity.User;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor
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
    private Long restock_round;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime sentAt;
}
