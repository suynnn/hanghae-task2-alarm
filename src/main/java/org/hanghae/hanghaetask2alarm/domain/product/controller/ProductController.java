package org.hanghae.hanghaetask2alarm.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.hanghae.hanghaetask2alarm.domain.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/{productId}/notifications/re-stock")
    public ResponseEntity<Void> triggerRestockNotification(@PathVariable("productId") Long productId) {

        // 재입고 알림을 전송하기 전, 상품의 재입고 회차를 1 증가 시킴
        productService.updateProductRestockRound(productId);



        return ResponseEntity.ok().build();
    }
}
