package org.hanghae.hanghaetask2alarm.domain.productNotificationHistory.service;

import lombok.RequiredArgsConstructor;
import org.hanghae.hanghaetask2alarm.domain.product.entity.Product;
import org.hanghae.hanghaetask2alarm.domain.product.exception.ProductNotFoundException;
import org.hanghae.hanghaetask2alarm.domain.product.repository.ProductRepository;
import org.hanghae.hanghaetask2alarm.domain.productNotificationHistory.entity.ProductNotificationHistory;
import org.hanghae.hanghaetask2alarm.domain.productNotificationHistory.entity.ProductNotificationStatus;
import org.hanghae.hanghaetask2alarm.domain.productNotificationHistory.repository.ProductNotificationHistoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductNotificationHistoryService {

    private final ProductNotificationHistoryRepository productNotificationHistoryRepository;
    private final ProductRepository productRepository;

    public void saveProductNotificationHistory(Long productId, Long userId, ProductNotificationStatus productNotificationStatus) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("해당 아이디의 상품을 찾을 수 없습니다."));

        ProductNotificationHistory productNotificationHistory = ProductNotificationHistory.builder()
                .product(product)
                .productNotificationStatus(productNotificationStatus)
                .lastSentUserId(userId)
                .build();

        productNotificationHistoryRepository.save(productNotificationHistory);
    }
}
