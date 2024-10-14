package org.hanghae.hanghaetask2alarm.common.restockalarm;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.hanghae.hanghaetask2alarm.domain.productUserNotification.entity.ProductUserNotification;
import org.hanghae.hanghaetask2alarm.domain.productUserNotificationHistory.entity.ProductUserNotificationHistory;
import org.hanghae.hanghaetask2alarm.domain.productUserNotificationHistory.service.ProductUserNotificationHistoryService;
import org.hanghae.hanghaetask2alarm.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ResolveAlarmService {

    private static final int INTERVAL_TOKEN_REFILL_SECONDS = 1;
    private static final int INTERVAL_TOKEN_REFILL_COUNT = 500;
    private static final int MAX_BANDWIDTH = 1000;

    private static final BlockingQueue<ProductUserNotification> notificationWaitingQueue = new LinkedBlockingQueue<>();

    private final Bucket bucket;
    private final ScheduledExecutorService scheduler;
    private final ProductUserNotificationHistoryService productUserNotificationHistoryService;

    public ResolveAlarmService(ProductUserNotificationHistoryService productUserNotificationHistoryService) {
        Refill intervalTokenRefill = Refill.intervally(INTERVAL_TOKEN_REFILL_COUNT, Duration.ofSeconds(INTERVAL_TOKEN_REFILL_SECONDS));
        Bandwidth intervalBandwidth = Bandwidth.classic(MAX_BANDWIDTH, intervalTokenRefill);

        this.bucket = Bucket.builder()
                .addLimit(intervalBandwidth)
                .build();

        this.scheduler = Executors.newScheduledThreadPool(1);
        this.productUserNotificationHistoryService = productUserNotificationHistoryService;

        startSendingNotifications();
    }

    // 알림을 큐에서 꺼내서 전송하는 작업을 1초마다 실행
    private void startSendingNotifications() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // 버킷에서 가져올 수 있는 토큰 수 500개
                long tokensAvailable = Math.min(bucket.getAvailableTokens(), INTERVAL_TOKEN_REFILL_COUNT);

                for (int i = 0; i < tokensAvailable; i++) {
                    ProductUserNotification notification = notificationWaitingQueue.poll(); // 큐에서 알림 대기 정보를 꺼냄
                    if (notification == null) {
                        break; // 큐가 비어있으면 반복 종료
                    }
                    bucket.tryConsume(1); // 토큰을 하나씩 소모

                    // 알림 전송 로직 호출
                    sendNotification(notification);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }, 0, INTERVAL_TOKEN_REFILL_SECONDS, TimeUnit.SECONDS);
    }

    @Transactional
    public User sendNotification(ProductUserNotification notification) {
        notification.updateNotificationReceiptStatus();

        ProductUserNotificationHistory productUserNotificationHistory = ProductUserNotificationHistory.builder()
                .product(notification.getProduct())
                .user(notification.getUser())
                .restockRound(notification.getProduct().getRestockRound())
                .build();

        productUserNotificationHistoryService.saveProductUserNotificationHistory(productUserNotificationHistory);

        return notification.getUser();
    }

    public void addNotificationToQueue(List<ProductUserNotification> productUserNotificationList) throws InterruptedException {

        for (ProductUserNotification productUserNotification : productUserNotificationList) {
            notificationWaitingQueue.put(productUserNotification);
        }
    }
}
