package org.hanghae.hanghaetask2alarm.domain.productUserNotificationHistory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanghae.hanghaetask2alarm.domain.productUserNotification.entity.ProductUserNotification;

@Entity
@Table
@Getter
@NoArgsConstructor
public class ProductUserNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_user_notification_id", nullable = false)
    private ProductUserNotification productUserNotification;
}
