package org.hanghae.hanghaetask2alarm.common.restockalarm;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.hanghae.hanghaetask2alarm.domain.notification.productNotificationHistory.entity.ProductNotificationStatus;
import org.hanghae.hanghaetask2alarm.domain.notification.productNotificationHistory.service.ProductNotificationHistoryService;
import org.hanghae.hanghaetask2alarm.domain.notification.productUserNotification.entity.ProductUserNotification;
import org.hanghae.hanghaetask2alarm.domain.notification.productUserNotificationHistory.entity.ProductUserNotificationHistory;
import org.hanghae.hanghaetask2alarm.domain.notification.productUserNotificationHistory.service.ProductUserNotificationHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class ResolveAlarmService {

    private static final int INTERVAL_TOKEN_REFILL_SECONDS = 1;
    private static final int INTERVAL_TOKEN_REFILL_COUNT = 500;
    private static final int MAX_BANDWIDTH = 1000;

    // 알림 대기 정보를 저장
    private static final BlockingQueue<ProductUserNotification> notificationWaitingQueue = new LinkedBlockingQueue<>();
    // 알림 이력을 저장
    private static final ConcurrentHashMap<Long, Long> checkNotiHistory = new ConcurrentHashMap<>();

    private final Bucket bucket;

    // 백그라운드 작업 스케줄러
    private final ScheduledExecutorService scheduler;
    private final ProductUserNotificationHistoryService productUserNotificationHistoryService;
    private final ProductNotificationHistoryService productNotificationHistoryService;

    // 생성자에서 주입된 서비스들 및 Bucket4j 설정 초기화
    public ResolveAlarmService(ProductUserNotificationHistoryService productUserNotificationHistoryService,
                               ProductNotificationHistoryService productNotificationHistoryService) {

        Refill intervalTokenRefill = Refill.intervally(INTERVAL_TOKEN_REFILL_COUNT, Duration.ofSeconds(INTERVAL_TOKEN_REFILL_SECONDS));
        Bandwidth intervalBandwidth = Bandwidth.classic(MAX_BANDWIDTH, intervalTokenRefill);

        this.bucket = Bucket.builder()
                .addLimit(intervalBandwidth)
                .build();

        // 스케줄링을 위한 스레드 풀 생성 (1개의 스레드로 작업 스케줄링)
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.productUserNotificationHistoryService = productUserNotificationHistoryService;
        this.productNotificationHistoryService = productNotificationHistoryService;

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

                    // 이전 상품들의 알림 전송 상태 저장하기 위한 구문
                    if (!checkNotiHistory.containsKey(notification.getProduct().getId())) {

                        for (Map.Entry<Long, Long> entry : checkNotiHistory.entrySet()) {
                            Long key = entry.getKey();
                            Long value = entry.getValue();

                            productNotificationHistoryService.saveProductNotificationHistory(key, value, ProductNotificationStatus.COMPLETED);
                        }

                        checkNotiHistory.clear();
                    }
                    checkNotiHistory.put(notification.getProduct().getId(), notification.getUser().getId());

                    bucket.tryConsume(1); // 토큰을 하나씩 소모

                    // 알림 전송 로직 호출
                    sendNotification(notification);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, INTERVAL_TOKEN_REFILL_SECONDS, TimeUnit.SECONDS); // 1초 간격으로 반복 실행
    }

    @Transactional
    public void sendNotification(ProductUserNotification notification) {
        notification.updateNotificationReceiptStatus();

        // 알림 수신 이력을 저장하기 위한 객체 생성
        ProductUserNotificationHistory productUserNotificationHistory = ProductUserNotificationHistory.builder()
                .product(notification.getProduct())
                .user(notification.getUser())
                .restockRound(notification.getProduct().getRestockRound())
                .build();

        productUserNotificationHistoryService.saveProductUserNotificationHistory(productUserNotificationHistory);
    }

    // 알림 대기 큐에 알림들을 추가
    public synchronized void addNotificationToQueue(List<ProductUserNotification> productUserNotificationList) throws InterruptedException {

        for (ProductUserNotification productUserNotification : productUserNotificationList) {
            notificationWaitingQueue.put(productUserNotification);
        }
    }
}
