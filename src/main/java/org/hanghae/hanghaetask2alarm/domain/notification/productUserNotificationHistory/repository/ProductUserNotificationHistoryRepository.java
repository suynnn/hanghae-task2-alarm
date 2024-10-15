package org.hanghae.hanghaetask2alarm.domain.notification.productUserNotificationHistory.repository;

import org.hanghae.hanghaetask2alarm.domain.notification.productUserNotificationHistory.entity.ProductUserNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUserNotificationHistoryRepository extends JpaRepository<ProductUserNotificationHistory, Long> {
}
