package org.hanghae.hanghaetask2alarm.domain.notification.productUserNotification.repository;

import org.hanghae.hanghaetask2alarm.domain.notification.productUserNotification.entity.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {

    List<ProductUserNotification> findAllByProductIdAndUpdatedAtIsNull(Long productId);
}
