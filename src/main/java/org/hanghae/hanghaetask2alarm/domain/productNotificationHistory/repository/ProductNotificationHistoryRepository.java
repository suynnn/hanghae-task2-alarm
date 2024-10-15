package org.hanghae.hanghaetask2alarm.domain.productNotificationHistory.repository;

import org.hanghae.hanghaetask2alarm.domain.productNotificationHistory.entity.ProductNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistory, Long> {
}
