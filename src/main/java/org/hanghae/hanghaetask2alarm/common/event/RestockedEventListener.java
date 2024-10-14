package org.hanghae.hanghaetask2alarm.common.event;

import lombok.RequiredArgsConstructor;
import org.hanghae.hanghaetask2alarm.common.restockalarm.ResolveAlarmService;
import org.hanghae.hanghaetask2alarm.domain.productUserNotification.entity.ProductUserNotification;
import org.hanghae.hanghaetask2alarm.domain.productUserNotification.service.ProductUserNotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestockedEventListener {

    private final ProductUserNotificationService productUserNotificationService;
    private final ResolveAlarmService resolveAlarmService;

    // 상품이 재입고 되었을 때, 재입고 알림을 설정한 유저들에게 알림 메시지 전달
    @EventListener
    public void sendRestockAlertsToSubscribedUsers(RestockedEvent restockedEvent) throws InterruptedException {
        List<ProductUserNotification> productUserNotificationList = productUserNotificationService.findAllByProductIdAndUpdatedAtIsNull(restockedEvent.getProductId());

        resolveAlarmService.addNotificationToQueue(productUserNotificationList);
    }

}
