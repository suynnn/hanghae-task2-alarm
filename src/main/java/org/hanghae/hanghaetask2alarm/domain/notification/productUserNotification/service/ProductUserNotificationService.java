package org.hanghae.hanghaetask2alarm.domain.notification.productUserNotification.service;

import lombok.RequiredArgsConstructor;
import org.hanghae.hanghaetask2alarm.domain.notification.productUserNotification.entity.ProductUserNotification;
import org.hanghae.hanghaetask2alarm.domain.notification.productUserNotification.repository.ProductUserNotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductUserNotificationService {

    private final ProductUserNotificationRepository productUserNotificationRepository;

    public List<ProductUserNotification> findAllByProductIdAndUpdatedAtIsNull(Long productId) {

        return productUserNotificationRepository.findAllByProductIdAndUpdatedAtIsNull(productId);
    }
}
