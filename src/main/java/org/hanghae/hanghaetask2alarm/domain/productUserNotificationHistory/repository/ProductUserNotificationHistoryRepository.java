package org.hanghae.hanghaetask2alarm.domain.productUserNotificationHistory.repository;

import org.hanghae.hanghaetask2alarm.domain.productUserNotificationHistory.entity.ProductUserNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUserNotificationHistoryRepository extends JpaRepository<ProductUserNotificationHistory, Long> {
}
