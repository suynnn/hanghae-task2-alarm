package org.hanghae.hanghaetask2alarm.domain.productUserNotificationHistory.service;

import lombok.RequiredArgsConstructor;
import org.hanghae.hanghaetask2alarm.domain.productUserNotificationHistory.entity.ProductUserNotificationHistory;
import org.hanghae.hanghaetask2alarm.domain.productUserNotificationHistory.repository.ProductUserNotificationHistoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductUserNotificationHistoryService {

    private final ProductUserNotificationHistoryRepository productUserNotificationHistoryRepository;

    public void saveProductUserNotificationHistory(ProductUserNotificationHistory productUserNotificationHistory) {

        productUserNotificationHistoryRepository.save(productUserNotificationHistory);
    }
}
